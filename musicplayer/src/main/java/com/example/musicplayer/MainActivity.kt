package com.example.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.aidlmodule.IVisualizer
import com.example.musicplayer.ui.theme.MusicPlayerTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var visualizerService: IVisualizer? = null
    private var bound = MutableStateFlow(false)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            visualizerService = IVisualizer.Stub.asInterface(service)
            bound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            bound.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 绑定到 Visualizer 服务
        val intent = Intent("com.example.hogotest.IVisualizer")
        intent.setPackage("com.example.hogotest")
        bindService(intent, connection, BIND_AUTO_CREATE)

        setContent {
            MusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicPlayerScreen(
                        onSessionIdUpdated = { sessionId ->
                            lifecycleScope.launch {
                                if (!bound.value) {
                                    val intent = Intent("com.example.hogotest.IVisualizer")
                                    intent.setPackage("com.example.hogotest")
                                    bindService(intent, connection, BIND_AUTO_CREATE)
                                    bound.first { it }
                                }
                                try {
                                    visualizerService?.updateMediaSessionId(sessionId)
                                    Log.i("MainActivity", "Updating visualizer sessionId: $sessionId")
                                    val visualizerIntent = Intent()
                                    visualizerIntent.component = ComponentName("com.example.hogotest", "com.example.hogotest.visualizer.VisualizerActivity")
                                    visualizerIntent.putExtra("sessionId", sessionId)
                                    startActivity(visualizerIntent)
                                } catch (e: Exception) {
                                    Log.e("MainActivity", "Error updating visualizer", e)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bound.value) {
            unbindService(connection)
            bound.value = false
        }
    }
}
