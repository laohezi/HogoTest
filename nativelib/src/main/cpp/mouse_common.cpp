//
// Created by 苗成林 on 2024/5/4.
//

#include "mouse_common.h"
#include <jni.h>
#include <string>
#include "libusb/libusb.h"
#include <android/log.h>
#define le16_to_cpu(x) libusb_cpu_to_le16(libusb_cpu_to_le16(x))

#define MOUSE_DEVICE_NUM_MAX (8)

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "libusb", __VA_ARGS__)
static void print_device(libusb_device *dev, libusb_device_handle *handle)
{
    struct libusb_device_descriptor dev_desc;
    unsigned char iManufacturer_string[256] = {0};
    unsigned char iProduct_string[256] = {0};
    unsigned char iSerialNumber_string[256] = {0};
    const char *speed;
    int ret;

    switch (libusb_get_device_speed(dev)) {
        case LIBUSB_SPEED_LOW:		speed = "1.5M"; break;
        case LIBUSB_SPEED_FULL:		speed = "12M"; break;
        case LIBUSB_SPEED_HIGH:		speed = "480M"; break;
        case LIBUSB_SPEED_SUPER:	speed = "5G"; break;
        case LIBUSB_SPEED_SUPER_PLUS:	speed = "10G"; break;
        default:			speed = "Unknown";
    }

    ret = libusb_get_device_descriptor(dev, &dev_desc);
    if (ret < 0) {
        LOGD( "failed to get device descriptor");
        return;
    }

//    if (!handle){
//        ret = libusb_open(dev, &handle);
//        if (ret < 0){
//            LOGD("Opend libusb device failed");
//        }
//    }

    LOGD("Dev (bus %03d, device %03d): ID %04x:%04x speed: %s\r\n" \
			, libusb_get_bus_number(dev), libusb_get_device_address(dev), \
			 dev_desc.idVendor, dev_desc.idProduct, speed);

    if (NULL != handle){
        if (dev_desc.iManufacturer) {
            ret = libusb_get_string_descriptor_ascii(handle, dev_desc.iManufacturer, iManufacturer_string, sizeof(iManufacturer_string));
            if (ret <= 0)
                LOGD("Get iManufacturer string descriptor failed %s\n", (char *)iManufacturer_string);
        }

        if (dev_desc.iProduct) {
            ret = libusb_get_string_descriptor_ascii(handle, dev_desc.iProduct, iProduct_string, sizeof(iProduct_string));
            if (ret <= 0)
                LOGD("Get iProduct string descriptor failed %s\n", (char *)iProduct_string);
        }

        if (dev_desc.iSerialNumber) {
            ret = libusb_get_string_descriptor_ascii(handle, dev_desc.iSerialNumber, iSerialNumber_string, sizeof(iSerialNumber_string));
            if (ret <= 0)
                LOGD("Get iManufacturer string descriptor failed %s\n", (char *)iSerialNumber_string);
        }
    }

    LOGD("Deivces Descriptor:\n");
    LOGD(" bLength: \t%9d\n", dev_desc.bLength);
    LOGD(" bDescriptorType:%8d\n", dev_desc.bDescriptorType);
    LOGD(" bcdUSB: \t%6x.%02x\n", dev_desc.bcdUSB >> 8, dev_desc.bcdUSB & 0xFF);
    LOGD(" bDeviceClass: \t%9d\n", dev_desc.bDeviceClass);
    LOGD(" bDeviceSubClass:%8d\n", dev_desc.bDeviceSubClass);
    LOGD(" bDeviceProtocol:%8d\n", dev_desc.bDeviceProtocol);
    LOGD(" bMaxPacketSize0:%8d\n", dev_desc.bMaxPacketSize0);
    LOGD(" idVendor: \t   0x%04x\n", dev_desc.idVendor);
    LOGD(" idProduct: \t   0x%04x\n", dev_desc.idProduct);
    LOGD(" bcdDevice: \t%6x.%02x\n", dev_desc.bcdDevice >> 8, dev_desc.bcdDevice & 0xFF);
    LOGD(" iManufacturer: \t%d %s\n", dev_desc.iManufacturer, iManufacturer_string);
    LOGD(" iProduct: \t\t%d %s\n", dev_desc.iProduct, iProduct_string);
    LOGD(" iSerialNumber: \t%d %s\n", dev_desc.iSerialNumber, iSerialNumber_string);
    LOGD(" bNumConfigurations: \t%d\n", dev_desc.bNumConfigurations);

//    if (handle)
//        libusb_close(handle);
}


static void print_configration(struct libusb_config_descriptor *conf_desc)
{
    LOGD("  Configuration Descriptor:\n");
    LOGD("   bLength: \t\t%9d\n", conf_desc->bLength);
    LOGD("   bDescriptorType: \t%9d\n", conf_desc->bDescriptorType);
    LOGD("   wTotalLength: \t   0x%04x\n", conf_desc->wTotalLength);
    LOGD("   bNumInterfaces: \t%9d\n", conf_desc->bNumInterfaces);
    LOGD("   bConfigurationValue: \t%d\n", conf_desc->bConfigurationValue);
    LOGD("   iConfiguration: \t%9d\n", conf_desc->iConfiguration);
    LOGD("   bmAttributes: \t     0x%x\n", conf_desc->bmAttributes);
    LOGD("   MaxPower: \t\t%9dmA\n", 2*conf_desc->MaxPower);
}

static void print_interface(struct libusb_interface_descriptor *if_desc,int if_num)
{
    LOGD("    Interface Descriptor: %d ", if_num);
    LOGD("     bLength: \t\t%9d\n", if_desc->bLength);
    LOGD("     bDescriptorType: \t%9d\n", if_desc->bDescriptorType);
    LOGD("     bInterfaceNumber: \t%9d\n", if_desc->bInterfaceNumber);
    LOGD("     bAlternateSetting: %9d\n", if_desc->bAlternateSetting);
    LOGD("     bNumEndpoints: \t%9d\n", if_desc->bNumEndpoints);
    LOGD("     bInterfaceClass: \t%9d\n", if_desc->bInterfaceClass);
    LOGD("     bInterfaceSubClass:%9d\n", if_desc->bInterfaceSubClass);
    LOGD("     bInterfaceProtocol:%9d\n", if_desc->bInterfaceProtocol);
    LOGD("     iInterface: \t%9d\n", if_desc->iInterface);
}

static void print_endpoint(struct libusb_endpoint_descriptor *ep_desc)
{
    static const char * const typeattr[] = {
            "Control",
            "Isochronous",
            "Bulk",
            "Interrupt"
    };
    static const char * const syncattr[] = {
            "None",
            "Asynchronous",
            "Adaptive",
            "Synchronous"
    };
    static const char * const usage[] = {
            "Data",
            "Feedback",
            "Implicit feedback Data",
            "(reserved)"
    };

    static const char * const hb[] = { "1x", "2x", "3x", "(?\?)" };
    unsigned wmax = le16_to_cpu(ep_desc->wMaxPacketSize);

    LOGD("      Endpoint Descriptor:\n"
         "        bLength             %5u\n"
         "        bDescriptorType     %5u\n"
         "        bEndpointAddress     0x%02x  EP %u %s\n"
         "        bmAttributes        %5u\n"
         "          Transfer Type            %s\n"
         "          Synch Type               %s\n"
         "          Usage Type               %s\n"
         "        wMaxPacketSize     0x%04x  %s %d bytes\n"
         "        bInterval           %5u\n",
         ep_desc->bLength,
         ep_desc->bDescriptorType,
         ep_desc->bEndpointAddress,
         ep_desc->bEndpointAddress & 0x0f,
         (ep_desc->bEndpointAddress & 0x80) ? "IN" : "OUT",
         ep_desc->bmAttributes,
         typeattr[ep_desc->bmAttributes & 3],
         syncattr[(ep_desc->bmAttributes >> 2) & 3],
         usage[(ep_desc->bmAttributes >> 4) & 3],
         wmax, hb[(wmax >> 11) & 3], wmax & 0x7ff,
         ep_desc->bInterval);
}

static  void release(libusb_device_handle *devh, int interface_num) {
    libusb_release_interface(devh, interface_num);
    libusb_close(devh);
    libusb_exit(NULL);

}