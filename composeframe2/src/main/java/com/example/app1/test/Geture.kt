package com.example.app1.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


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

@Composable
fun NestedScroll() {

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
@Preview
@Composable
fun DragPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        var offsetX by remember {
            mutableStateOf(0)
        }
        var offsetY by remember {
            mutableStateOf(0)
        }

        Box(modifier = Modifier
            .offset { IntOffset(offsetX, offsetY) }
            .background(Color.Green)
            .size(50.dp)
            .pointerInput(Unit){
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x.roundToInt()
                    offsetY += dragAmount.y.roundToInt()
                }
            }
        )


    }
}

