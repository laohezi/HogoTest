package com.example.hogotest

import android.app.Application
import com.hugo.mylibrary.components.BaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : BaseApp(){
    override fun onCreate() {
        super.onCreate()
       // Thread.sleep(3000)
    }
}