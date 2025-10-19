package com.example.hogotest

import android.app.Application
import com.example.hogotest.di.visualizerModule
import com.hugo.mylibrary.components.BaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(visualizerModule)
        }
    }
}

