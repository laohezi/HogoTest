package com.example.hogotest.visualizer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aidlmodule.IVisualizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisualizerViewModel(application: Application) : AndroidViewModel(application) {
    private val _visualizerColor = MutableStateFlow(Color.Black)
    val visualizerColor: StateFlow<Color> = _visualizerColor.asStateFlow()

    private val _visualizerAlpha = MutableStateFlow(1.0f)
    val visualizerAlpha: StateFlow<Float> = _visualizerAlpha.asStateFlow()

    private var visualizerService: IVisualizer? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            visualizerService = IVisualizer.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            visualizerService = null
        }
    }

    val fftProcessorNames: List<String> = FFTDataProcessorFactory.processors.keys.toList()

    init {
        VisualizerService.colorUpdateCallback = { color, alpha ->
            _visualizerColor.value = color
            _visualizerAlpha.value = alpha
        }
        val intent = Intent(application, VisualizerService::class.java)
        application.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun setFftProcessor(name: String) {
        viewModelScope.launch {
            visualizerService?.setFftProcessor(name)
        }
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unbindService(connection)
    }
}
