package com.example.nativelib

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.nativelib.NativeLib

import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class JNIActivity : Activity() {

    val nativeLib = NativeLib()
    val data = MutableLiveData("")


    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val array = arrayOf("333", "222", "111")
        textView = TextView(this, null)
        var text = "yuanshi"
        nativeLib.errorInJava()
        text = nativeLib.stringFromJNIObject()
        text = nativeLib.stringFromJNIObject()
        try {


        } catch (e: Exception) {
            e.printStackTrace()
        }
        textView.setText(text)

        setContentView(textView)


    }

    override fun onResume() {
        super.onResume()
        Log.d("huhuhu", "onResume")

        // startActivity()
        // startActivity()


    }


}




