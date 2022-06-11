package com.example.app1.test

import android.annotation.SuppressLint
import android.app.ActionBar
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope

@Preview
@Composable
fun ScaffoldPreview(
) {

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    ScaffoldTest(scaffoldState = scaffoldState, scope = scope)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldTest(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("I am top bar") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Menu, contentDescription =null )

                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text(text = "BUTTON") }, onClick = { /*TODO*/ })
        },
        bottomBar = {}
    ) {

        Text(text = "Hellow World")
    }

}



