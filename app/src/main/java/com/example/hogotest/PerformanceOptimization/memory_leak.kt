package com.example.hogotest.PerformanceOptimization

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import kotlin.concurrent.thread

class  ThreadLeakTestActivity:AppCompatActivity() {
    val Tag = "ThreadLeakTestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = {
                startActivity(Intent(this,ThreadLeakTestActivity::class.java))
            }) {
                Text("新建activity")
            }
        }
        thread{
            while (true){
                Thread.sleep(1000)
                Log.d(this.toString(), "thread is runing" )
            }
        }
    }
}