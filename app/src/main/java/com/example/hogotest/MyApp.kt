package com.example.hogotest

import android.app.Application

import com.hugo.mylibrary.components.BaseApp
import com.hugo.mylibrary.utils.logg
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : BaseApp(){
    override fun onCreate() {
        super.onCreate()
         logg.init(getLogPrefix())
       // Thread.sleep(3000)
    }

    fun getLogPrefix():String{

        return "HugoTest"
    }
}