//
// Created by miaoc on 2024/4/28.
//
#include <jni.h>
#include <string>
#include "libusb/libusb.h"
#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "libusb", __VA_ARGS__)


void print_device(libusb_device *pDevice, libusb_device_handle *pHandle);

void release(libusb_device_handle *devh, int interface_num);

void test_libusb(jint vid, jint pid, int fileDescriptor) {


}


int runing = 1;

extern "C"
JNIEXPORT int JNICALL
Java_com_example_nativelib_UsbLib_openDevice(JNIEnv *env, jobject thiz, jint vid, jint pid,
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
    int endpoint;
    int interface_num = 0;
    int found = 0;
    int transferred;
    int count = 0;
    int bufferLen = 32;
    unsigned char buffer[bufferLen];
    struct libusb_config_descriptor *config_desc;

    /* parse interface descriptor, find usb mouse */
    err = libusb_get_config_descriptor(libusb_get_device(devh), 0, &config_desc);
    if (err) {
        LOGD("could not get configuration descriptor\n");
        return err;
    }
    LOGD("libusb_get_config_descriptor() ok\n");

    for (int interface = 0; interface < config_desc->bNumInterfaces; interface++) {
        const struct libusb_interface_descriptor *intf_desc = &config_desc->interface[interface].altsetting[0];
        interface_num = intf_desc->bInterfaceNumber;
        if (intf_desc->bInterfaceClass == 3 && intf_desc->bInterfaceProtocol == 2) {
            LOGD("find usb dev ok\n");
            for (int ep = 0; ep < intf_desc->bNumEndpoints; ep++) {
                if ((intf_desc->endpoint[ep].bmAttributes & 3) == LIBUSB_TRANSFER_TYPE_INTERRUPT &&
                    (intf_desc->endpoint[ep].bEndpointAddress & 0x80) == LIBUSB_ENDPOINT_IN) {
                    /* 找到了输入的中断端点 */

                    endpoint = intf_desc->endpoint[ep].bEndpointAddress;
                    bufferLen = intf_desc->endpoint[ep].wMaxPacketSize;
                    LOGD("find in int endpoint",endpoint);
                    found = 1;
                    break;
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

//    if (found){
//      err =   libusb_open(dev,&devh);
//        if (err){
//
//            LOGD("failed to libusb_open--%d",err);
//            release(devh, interface_num);
//            return  err;
//        }
//    }
   // libusb_free_device_list(&dev,1);

    /* enable automatic attach/detach kernel driver on supported platforms in libusb */
    libusb_set_auto_detach_kernel_driver(devh, 1);
   // libusb_detach_kernel_driver(devh,interface_num);

    /* claim interface */
    err = libusb_claim_interface(devh, interface_num);
    if (err) {
        LOGD("failed to libusb_claim_interface\n");
        return err;
    }
    LOGD("libusb_claim_interface ok\n");

    while (runing) {
        err = libusb_interrupt_transfer(devh, endpoint, buffer, bufferLen, &transferred, 5000);
        if (!err) {
            /* parser data */
            LOGD("%04d count: ", count++);
            LOGD("data: %s", buffer);
        } else if (err == LIBUSB_ERROR_TIMEOUT) {
            LOGD("libusb_interrupt_transfer timout\n");
        } else {
            LOGD("libusb_interrupt_transfer err : %d\n", err);
            break;
        }
    }
    release(devh, interface_num);

    return r;
}

void release(libusb_device_handle *devh, int interface_num) {
    libusb_release_interface(devh, interface_num);
    libusb_close(devh);
    libusb_exit(NULL);
}

void print_device(libusb_device *pDevice, libusb_device_handle *pHandle) {

    LOGD("libusb_get_device_address : %d", libusb_get_device_address(pDevice));
    LOGD("libusb_get_device_speed : %d", libusb_get_device_speed(pDevice));
    LOGD("libusb_get_max_packet_size : %d", libusb_get_max_packet_size(pDevice, 0));
    LOGD("libusb_get_max_iso_packet_size : %d", libusb_get_max_iso_packet_size(pDevice, 0));
    LOGD("libusb_get_max_alt_packet_size : %d", libusb_get_max_alt_packet_size(pDevice, 0, 0, 0));

}




