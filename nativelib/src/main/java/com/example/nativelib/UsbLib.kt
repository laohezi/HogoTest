package com.example.nativelib

class UsbLib {
    external fun openDevice(vid:Int,pid:Int,fd:Int):Int
    external fun openDeviceAsync(vid:Int,pid:Int,fd:Int):Int

    companion object{
        init {
            System.loadLibrary("nativelib")
        }
    }
}