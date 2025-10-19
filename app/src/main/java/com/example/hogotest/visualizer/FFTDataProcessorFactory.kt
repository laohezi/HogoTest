package com.example.hogotest.visualizer

object FFTDataProcessorFactory {
    val processors: Map<String, FFTDataProcessor> = mapOf(
        "Gentle" to GentleFFTDataProcessor(),
        "Rhythmic" to RhythmicFFTDataProcessor(),
        "Default" to DefaultFFTDataProcessor()
    )

    fun get(name: String): FFTDataProcessor? {
        return processors[name]
    }
}

