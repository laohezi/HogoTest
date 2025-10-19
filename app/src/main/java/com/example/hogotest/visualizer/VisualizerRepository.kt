package com.example.hogotest.visualizer

import android.media.audiofx.Visualizer
import android.util.Log
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class VisualizerData(val color: Color, val alpha: Float)

class VisualizerRepository(private val coroutineScope: CoroutineScope) {
    private val _visualizerData = MutableStateFlow(VisualizerData(Color.Black, 1.0f))
    val visualizerData: StateFlow<VisualizerData> = _visualizerData

    // From FftProcessorRepository
    val fftProcessorNames: List<String> = FFTDataProcessorFactory.processors.keys.toList()
    private var fftProcessor: FFTDataProcessor = RhythmicFFTDataProcessor()

    private val usePolling = false // true for polling, false for listener
    private var fftDataProvider: FftDataProvider? = null

    private var fftChannel: Channel<ByteArray>? = null
    private var consumerJob: Job? = null

    init {
    }


    suspend fun startDataCapture(visualizer: Visualizer) {
        stopDataCapture() // Stop any existing capture first

        val newChannel = Channel<ByteArray>(capacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        fftChannel = newChannel

        consumerJob = coroutineScope.launch {
            for (fft in newChannel) {
                fftProcessor.process(fft)?.let { result ->
                    updateVisualizerData(result)
                }
            }
        }

        fftDataProvider = if (usePolling) {
            PollingFftDataProvider(coroutineScope)
        } else {
            ListenerFftDataProvider()
        }

        fftDataProvider?.start(visualizer) { fft ->
            processFFTData(fft)
        }
    }

    fun stopDataCapture() {
        if (fftChannel?.isClosedForSend == false) {
            Log.d("VisualizerRepository", "stopDataCapture called", Exception())
            fftDataProvider?.stop()
            fftDataProvider = null
            fftChannel?.close()
        }
        consumerJob?.cancel()
        consumerJob = null
        fftChannel = null
    }

    fun setFftProcessor(name: String) {
        FFTDataProcessorFactory.get(name)?.let {
            fftProcessor = it
        }
    }

    fun processFFTData(fft: ByteArray) {
        val currentChannel = fftChannel
        if (currentChannel == null || currentChannel.isClosedForSend) {
            Log.d("VisualizerRepository", "Channel is not ready or closed, dropping data")
            return
        }
        if (!currentChannel.trySend(fft).isSuccess) {
            Log.d("VisualizerRepository", "Channel is full, dropping oldest data")
        }
    }

    private fun updateVisualizerData(data: VisualizerData) {
        _visualizerData.value = data
    }

}
