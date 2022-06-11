package com.example.app1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun TapSample() {
    var event = remember {
        mutableStateOf("tinit")
    }
    Text(
        text = event.value, modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { event.value = "double tap" },
                    onTap = { event.value = "tap" },
                    onLongPress = { event.value = "long press" },
                    onPress = { event.value = "press" }

                )
            }
            .padding(16.dp)
    )
}


@Preview(heightDp = 500)
@Composable
fun NestedScroll() {
   /* val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(modifier = Modifier
        .background(color = Color.LightGray)
        .padding(24.dp)
        .verticalScroll(rememberScrollState())
    ){
        Column() {
            repeat(6){
                Box(modifier = Modifier
                    .height(128.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
                ){
                    Text(text = "Scroll Here", modifier = Modifier
                        .border(4.dp, Color.Gray)
                        .verticalScroll(rememberScrollState())
                        .background(brush = gradient)
                        .height(200.dp)
                    )

                }
            }
        }
    }*/

    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }

}

