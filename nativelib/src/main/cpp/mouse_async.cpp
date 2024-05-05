//
// Created by 苗成林 on 2024/5/4.
//
#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include "libusb/libusb.h"
#include <android/log.h>
#include "mouse_common.cpp"

struct usb_mouse {
    struct libusb_device_handle *handle;
    int interface;
    int endpoint;
    unsigned char buf[16];
    int transferred;
    struct libusb_transfer *transfer;
    struct usb_mouse *next;
};

static struct usb_mouse *usb_mouse_list;



void transfer_cb(struct libusb_transfer *transfer) {
    static int count = 0;
    if (transfer->status == LIBUSB_TRANSFER_COMPLETED)
    {
        /* parser data */
        LOGD("%04d datas: ", count++);
        for (int i = 0; i < transfer->actual_length; i++)
        {
            LOGD("%02x ", transfer->buffer[i]);
        }
        LOGD("\n");

    }

    if (int err =libusb_submit_transfer(transfer) < 0)
    {
        LOGD("libusb_submit_transfer err %d",err);
    }

}


extern "C"
JNIEXPORT jint JNICALL
Java_com_example_nativelib_UsbLib_openDeviceAsync(JNIEnv *env, jobject thiz, jint vid, jint pid,
                                                  jint fd) {

    LOGD("test_libusb");
    libusb_context *ctx = NULL;
    libusb_device_handle *devh = NULL;
    int r = 0;
    r = libusb_set_option(ctx, LIBUSB_OPTION_NO_DEVICE_DISCOVERY, NULL);
    if (r != LIBUSB_SUCCESS) {
        LOGD("libusb_set_option failed: %d\n", r);
        return -1;
    }
    r = libusb_init(&ctx);
    libusb_set_option(ctx, LIBUSB_OPTION_LOG_LEVEL, LIBUSB_LOG_LEVEL_DEBUG);
    if (r < 0) {
        LOGD("libusb_init failed: %d\n", r);
        return r;
    }
    r = libusb_wrap_sys_device(ctx, (intptr_t) fd, &devh);
    if (r < 0) {
        LOGD("libusb_wrap_sys_device failed: %d\n", r);
        return r;
    } else if (devh == NULL) {
        LOGD("libusb_wrap_sys_device returned invalid handle\n");
        return r;
    }
    struct libusb_device *dev = libusb_get_device(devh);

    print_device(dev, devh);
    int err ;
    char endpoint;
    int interface_num = 0;
    int found = 0;
    int transferred;
    int count = 0;
    int bufferLen = 128;
    unsigned char buffer[bufferLen];
    struct libusb_config_descriptor *config_desc;

    /* parse interface descriptor, find usb mouse */
    err = libusb_get_config_descriptor(dev, 0, &config_desc);
    if (err) {
        LOGD("could not get configuration descriptor\n");
        return err;
    }
    LOGD("libusb_get_config_descriptor() ok\n");

    for (int interface = 0; interface < config_desc->bNumInterfaces; interface++) {
        struct libusb_interface_descriptor *intf_desc = const_cast<libusb_interface_descriptor *>(&config_desc->interface[interface].altsetting[0]);
        interface_num = intf_desc->bInterfaceNumber;
        print_interface(intf_desc, interface_num);
        if (intf_desc->bInterfaceClass == 3 && intf_desc->bInterfaceProtocol == 2) {
            LOGD("find usb dev ok\n");

            for (int ep = 0; ep < intf_desc->bNumEndpoints; ep++) {
                struct  libusb_endpoint_descriptor ep_desc = intf_desc->endpoint[ep];
                if ((ep_desc.bmAttributes & 3) == LIBUSB_TRANSFER_TYPE_INTERRUPT &&
                    (ep_desc.bEndpointAddress & 0x80) == LIBUSB_ENDPOINT_IN) {
                    /* 找到了输入的中断端点 */

                    endpoint = ep_desc.bEndpointAddress;
                    bufferLen = ep_desc.wMaxPacketSize;
                    LOGD("find in int endpoint %d",endpoint);
                    found = 1;
                    print_endpoint(&ep_desc);

                }
            }
        }
    }

    libusb_free_config_descriptor(config_desc);

    if (!found) {
        libusb_release_interface(devh, interface_num);
        libusb_close(devh);
        libusb_exit(NULL);
        return -1;
    }

    libusb_detach_kernel_driver(devh,interface_num);

    /* claim interface */
    err = libusb_claim_interface(devh, interface_num);
    if (err != 0) {
        LOGD("failed to libusb_claim_interface--%d",err);
        return err;
    }
    LOGD("libusb_claim_interface ok %d",interface_num);
    struct libusb_transfer *transfer = libusb_alloc_transfer(0);

    libusb_fill_interrupt_transfer(transfer,devh,endpoint,buffer,bufferLen,transfer_cb,NULL,1000);
    while (1) {
        struct timeval tv = { 5, 0 };
        int r;

        r = libusb_handle_events_timeout(ctx, &tv);
        if (r < 0) {
            LOGD( "libusb_handle_events_timeout err\n");
            break;
        }
    }
}

