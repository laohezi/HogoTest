package com.example.nativelib

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

class JNIActivity : Activity(){
    val nativeLib = NativeLib()
    val usbLib = UsbLib()
    val data = MutableLiveData("")


    lateinit var textView :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            setOnClickListener {
                usbLib.printDevice()
            }
        })

    }

}


