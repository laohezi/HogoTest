package com.example.hogotest.test

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun MyScreen(

    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    // If the UI state contains an error, show snackbar


    // `LaunchedEffect` will cancel and re-launch if
    // `scaffoldState.snackbarHostState` changes
    LaunchedEffect(scaffoldState.snackbarHostState) {
        // Show snackbar using a coroutine, when the coroutine is cancelled the
        // snackbar will automatically dismiss. This coroutine will cancel whenever
        // `state.hasError` is false, and only start when `state.hasError` is true
        // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
        delay(200)
        scaffoldState.snackbarHostState.showSnackbar(
            message = "Error message",
            actionLabel = "Retry message"
        )
    }


    Scaffold(snackbarHost = { scaffoldState.snackbarHostState }) {
        Text(modifier = Modifier.clickable {

        },
            text = "Hello"
        )

    }
}