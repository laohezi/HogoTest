package com.example.hogotest.jni

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.repeatOnLifecycle
import com.example.nativelib.NativeLib
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class JNIActivity :AppCompatActivity(){
    val nativeLib = NativeLib()
    val data = MutableLiveData("")


    lateinit var textView :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val array = arrayOf("333","222","111")
        textView = TextView(this,null)
        var text = "yuanshi"
        nativeLib.errorInJava()
       text = nativeLib.stringFromJNIObject()
        try {


        }catch (e:Exception){
            e.printStackTrace()
        }
        textView.setText(text)

        setContentView(textView)
        textView.rPrintlnParent()






    }

    override fun onResume() {
        super.onResume()
        Log.d("huhuhu","onResume")
        textView.rPrintlnParent()
        val liveData = MutableLiveData<String>()
        liveData.value = ""

       // startActivity()


    }




}



fun View.rPrintlnParent(){

    var parent = parent
    while (parent!=null){
        Log.d("huhuhu","this is $this,and parent is $parent")
        parent = parent.parent
    }

}