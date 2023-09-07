package com.hugo.mylibrary.components

import android.app.Application

lateinit var application: BaseApp

open class BaseApp():Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}