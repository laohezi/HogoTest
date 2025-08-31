package com.example.hogotest

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hugo.mylibrary.utils.getAllActivities

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Actictys()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Actictys() {
    val activitys = getAllActivities().filter { !it.name.contains("Instrumentation") }
    val activity = LocalContext.current as Activity
    FlowRow(
        modifier = Modifier.fillMaxSize(),
    //    horizontalArrangement = Arrangement.SpaceBetween
    ) {
        activitys.forEach { it ->
            val name = remember {
                it.name.split(".").last().replace("Test", "").replace("Activity", "")
            }
            var readyToDraw by remember { mutableStateOf(false) }
            val rawSize = 13.sp
            var compatSize by remember { mutableStateOf(rawSize) }
            Button(
                onClick = {
                    activity.startActivity(Intent().apply {
                        setComponent(ComponentName(it.packageName, it.name))
                    })
                },
                modifier = Modifier
                    .height(40.dp)
            ) {
                Text(
                    text = name,
                    fontSize = compatSize,
                    modifier = Modifier.drawWithContent {
                        if (readyToDraw) {
                            drawContent()
                        }
                    },
                    onTextLayout = { result ->
                        if (result.hasVisualOverflow) {
                            compatSize *= 0.9f
                        } else {
                            readyToDraw = true
                        }
                    }
                )
            }
        }
    }
}
