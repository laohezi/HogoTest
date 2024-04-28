//
// Created by miaoc on 2024/4/28.
//
#include <jni.h>
#include "libusb/libusb.h"
#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "UsbLib", __VA_ARGS__)
void test_libusb(jint vid,jint pid,int fileDescriptor){





}

extern "C"
JNIEXPORT int JNICALL
 Java_com_example_nativelib_UsbLib_printDevice(JNIEnv *env, jobject thiz) {
    LOGD("test_libusb");
    libusb_context *ctx = NULL;
    libusb_device_handle *devh = NULL;
    int r = 0;

//    r = libusb_set_option(NULL, LIBUSB_OPTION_NO_DEVICE_DISCOVERY, NULL);
//    if (r != LIBUSB_SUCCESS) {
//        LOGD("libusb_set_option failed: %d\n", r);
//        return -1;
//    }
    r = libusb_init(&ctx);
    if (r < 0) {
        LOGD("libusb_init failed: %d\n", r);
        return r;
    }
    return r;
//    r = libusb_wrap_sys_device(ctx, (intptr_t) fileDescriptor, &devh);
//    if (r < 0) {
//        LOGD("libusb_wrap_sys_device failed: %d\n", r);
//        return r;
//    } else if (devh == NULL) {
//        LOGD("libusb_wrap_sys_device returned invalid handle\n");
//        return r;
//    }
//    libusb_get_device(devh)

}