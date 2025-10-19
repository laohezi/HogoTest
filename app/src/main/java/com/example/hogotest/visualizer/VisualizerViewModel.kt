package com.example.hogotest.visualizer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class VisualizerViewModel(
    private val visualizerRepository: VisualizerRepository
) : ViewModel() {
    val visualizerData: StateFlow<VisualizerData> = visualizerRepository.visualizerData

    val fftProcessorNames: List<String> = visualizerRepository.fftProcessorNames


    fun setFftProcessor(name: String) {
        visualizerRepository.setFftProcessor(name)
    }
}
