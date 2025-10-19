package com.example.hogotest.visualizer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class VisualizerViewModel(
    private val visualizerRepository: VisualizerRepository,
    private val fftProcessorRepository: FftProcessorRepository
) : ViewModel() {
    val visualizerData: StateFlow<VisualizerData> = visualizerRepository.visualizerData

    val fftProcessorNames: List<String> = fftProcessorRepository.fftProcessorNames


    fun setFftProcessor(name: String) {
        fftProcessorRepository.setFftProcessor(name)
    }
}
