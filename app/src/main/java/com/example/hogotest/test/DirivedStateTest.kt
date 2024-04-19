package com.example.hogotest.test

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
private val TAG = "DirivedStateTest"
@Preview
@Composable
fun RestrictionForRemember(){
    val names = remember {
        mutableStateListOf("oppo","vivo","xiaomi","huawei")
    }
   val lowerNames by remember {
         derivedStateOf {
             names.map { it.uppercase() }
         }
    }

    var count by remember { mutableStateOf(0) }

//  val lowerNames = names.map { "$it" }

    Log.d(TAG, "RestrictionForRemember: names is ${names.toTypedArray().contentToString()}")

    Column {
        Text(text = "count is $count", modifier = Modifier.clickable {
            count += 1
        })

        Text(text = "add google", modifier = Modifier.clickable {
           names.add("google")
        })
        LazyColumn {
            items(lowerNames) {
                Log.d(TAG, "RestrictionForRemember: it is $it")
                LoggedText(text = it)
            }

            items(lowerNames) {
                LoggedText(text = it)
            }

        }
    }
}
@Composable
fun LoggedText(text:String,modifier: Modifier = Modifier){
    Log.d(TAG, "LoggedText: $text")
    Text(text = text, modifier = modifier)

}