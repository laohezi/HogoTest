package com.example.hogotest

import android.app.Application
lateinit var myapp: Application
class MyAPp : Application(){

    override fun onCreate() {
        myapp = this
        super.onCreate()


    }
}