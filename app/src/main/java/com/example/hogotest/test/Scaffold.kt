package com.example.hogotest.test

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.log

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
    val tag = "ScaffoldTest"
    Log.d(tag,"run ScaffoldTest")
    var showSnackBar  by remember { mutableStateOf(false) }
    var showDrawer  by remember { mutableStateOf(false) }
    var times =  remember { 0 }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("I a top bar") },
                navigationIcon = {
                    IconButton(onClick = {
//                       scope.launch {
//                         Log.d(tag, "onClickNavigationIcon")
//                           if (scaffoldState.drawerState.isOpen){
//                               scaffoldState.drawerState.close()
//                           }else{
//                               scaffoldState.drawerState.open()
//                           }
//
//                       }

                        showDrawer = !showDrawer

                    }) {
                        Icon(Icons.Filled.Menu, contentDescription =null )

                    }
                }
            )
        },
        snackbarHost = {
                    SnackbarHost(hostState = scaffoldState.snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(text = { Text(text = "BT") }, onClick = {
                Log.d(tag, "onClickFloatingActionButton")
//                scope.launch {
//                    scaffoldState.snackbarHostState.showSnackbar(
//                        message = "Error message",
//                    )
//                }
                showSnackBar = true

            })
        },
        drawerContent = {
            Text(text = "Drawer")
        }
    ) {

        Log.d(tag,"times changed to $times")
        Text(text = times++.toString())
        LaunchedEffect(showDrawer) {
            Log.d(tag, "LaunchedEffect")
            if (showSnackBar){
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Error message",
                    actionLabel = "Retry message"
                )

                showSnackBar = false

            }
            if (showDrawer){
                scaffoldState.drawerState.open()
            }
        }
    }

}



