package com.example.hogotest.PerformanceOptimization

import android.os.Looper
import android.util.Printer

class MyBlockCanary{
    @Volatile
    var isStarted = false

    fun start(){
        if (!isStarted){
            Looper.getMainLooper().setMessageLogging(LooperMonitor())
        }


    }


    inner class LooperMonitor : Printer{
        override fun println(x: String?) {
        }

    }

}



