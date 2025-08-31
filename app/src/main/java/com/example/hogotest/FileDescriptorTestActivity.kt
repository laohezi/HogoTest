package com.example.hogotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hogotest.ui.FileDescriptorTestScreen

class FileDescriptorTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileDescriptorTestScreen()
        }
    }
}

