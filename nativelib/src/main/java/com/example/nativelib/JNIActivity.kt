package com.example.nativelib

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

class JNIActivity : Activity(){
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