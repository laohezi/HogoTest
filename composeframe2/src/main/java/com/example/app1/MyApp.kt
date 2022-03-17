package com.example.app1

import android.app.Application
import java.util.*

lateinit  var appContext:Application

lateinit var  sessionId :String

class MyApp :Application(){
    override fun onCreate() {
        super.onCreate()
        appContext = this
        sessionId = UUID.randomUUID().toString()
    }
}