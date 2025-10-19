package com.example.hogotest.visualizer

import com.example.hogotest.visualizer.RhythmicFFTDataProcessor

class FftProcessorRepository {

    val fftProcessorNames: List<String> = FFTDataProcessorFactory.processors.keys.toList()

    var fftProcessor: FFTDataProcessor = RhythmicFFTDataProcessor()

    fun setFftProcessor(name: String) {
        FFTDataProcessorFactory.get(name)?.let {
            fftProcessor = it
        }
    }
}
