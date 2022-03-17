package com.example.app1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun MyRow(){
    var inc by remember {
        mutableStateOf(1)
    }
    inc += 1
    println("lalala-myrow${inc}")
}

@Composable
fun MyRow(text:String){
    var rawInc  by remember {
        mutableStateOf(1)
    }
    Row() {
        rawInc += 1
        Text(text = "$text --- $rawInc")

    }
}

@Composable
fun Test(){
    Column() {
        println("lalala clolum")
        var text by remember {
            mutableStateOf("lala")
        }
        var inc by remember {
            mutableStateOf(1)
        }

        Button(onClick = {
            text += "lala"
            inc += 1
        }) {
            Text(text = text)
            println("lalala--$inc")
            Text(text = inc.toString())
            MyRow()
        }

        MyRow(text = text)
    }
}