package com.example.app1.test

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun MyBassicColumnPreview(){
    MyBasicColumn() {
        Text(text = "lalala")
        Text(text = "hahaha")
        Text(text = "yoyoyo")
    }
}

@Composable
fun MyBasicColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Layout(modifier = modifier,content = content){ measurables,constraints->
        val placeables = measurables.map {
            it.measure(constraints)
        }
        layout(constraints.maxWidth,constraints.maxHeight){
            var y = 0
            placeables.forEach {
                it.placeRelative(x=0,y=y)
                y += it.height
            }
        }

    }

}



