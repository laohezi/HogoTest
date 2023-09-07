package com.example.hogotest

import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hugo.mylibrary.utils.getAllActivities


import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TextView(this).post{}
        setContent {
            Actictys()
        }
    }
}

@Composable
fun Actictys() {
    val activitys = getAllActivities().filter { !it.name.contains("Instrumentation") }
    val activity = LocalContext.current as Activity
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {

        items(activitys.size) { position ->
            val it = activitys[position]
            val name = remember {
                it.name.split(".").last().replace("Test", "").replace("Activity", "")
            }

            var readyToDraw by remember {
                mutableStateOf(false)
            }
            var rawSize = 13.sp
            var compatSize by remember {
                mutableStateOf(rawSize)
            }

            Button(
                onClick = {
                    activity.startActivity(Intent().apply {
                        setComponent(ComponentName(it.packageName, it.name))
                    })
                },
                Modifier
                    .padding(2.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {

                Text(text = name,
                   fontSize = compatSize,

                    modifier = Modifier
                      //  .verticalScroll(rememberScrollState(), true)
                        .drawWithContent {
                            if (readyToDraw) {
                                drawContent()
                            }
                        },
                    onTextLayout = { result ->
                        if(result.hasVisualOverflow){
                            compatSize *= 0.9f
                        }else{
                            readyToDraw = true
                        }

                    }
                )
            }
        }


    }


}

