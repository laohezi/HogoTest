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
        try {
           // text = nativeLib.getSecondNumberFromJNI(array)

        }catch (e:Exception){
            e.printStackTrace()
        }
        textView.setText(text)

        setContentView(textView)
        textView.rPrintlnParent()




        val client =  OkHttpClient.Builder()
            .connectTimeout(120,TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .addNetworkInterceptor(LogcatInterceptor())
            .build()
        textView.setOnClickListener {
            thread {
                try {
                    val request = Request.Builder().url("https://www.baidu.com").build()

                    val response = client.newCall(request).execute()
                    Log.d("lalal", response.toString() + "--" + response.body())
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                }
            }
        }


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

class LogcatInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("okhttp request is:",request.toString())
        val response = chain.proceed(request)
        Log.d(" okhttp response is:",response.body()!!.string())
        return  response
    }
}

class CacheInterceptor:Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return  response
    }

}


fun View.rPrintlnParent(){

    var parent = parent
    while (parent!=null){
        Log.d("huhuhu","this is $this,and parent is $parent")
        parent = parent.parent
    }

}