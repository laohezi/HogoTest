package com.example.hogotest.PerformanceOptimization

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf



class ClickAnrTest:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Thread.sleep(100000)
       // startService()
        setContent {

        }

    }

}
@Preview
@Composable
fun Content(){
    Column {
      Button(onClick = {
          Thread.sleep(30000)

      }) {

      }
    }
}

@Preview
@Composable
fun LockContent(){
    val countDownLatch = remember {
        CountDownLatch(20)
    }
    var buttonColors by remember { mutableStateOf(
       Color.Red
    )
    }
    Column {
        Button(onClick = {
            thread {
                while (true){
                    Thread.sleep(1000)
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()
            buttonColors = Color.DarkGray




        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonColors
        )) {

        }
    }

}





