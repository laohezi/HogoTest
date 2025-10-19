package com.example.hogotest.visualizer

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class VisualizerData(val color: Color, val alpha: Float)

class VisualizerRepository() {
    private val _visualizerData = MutableStateFlow(VisualizerData(Color.Black, 1.0f))
    val visualizerData: StateFlow<VisualizerData> = _visualizerData

    fun updateVisualizerData(data: VisualizerData) {
        _visualizerData.value = data
    }

}
