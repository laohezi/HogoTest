package com.example.hogotest.visualizer

import android.media.audiofx.Visualizer
import android.util.Log
import kotlinx.coroutines.*

interface FftDataProvider {
    suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit)
    fun stop()
}

class ListenerFftDataProvider : FftDataProvider {
    private var listener: Visualizer.OnDataCaptureListener? = null

    override suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit) {
        listener = object : Visualizer.OnDataCaptureListener {
            override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {}

            override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
         //       Log.i("ListenerFftDataProvider", "FFT data captured")
                fft?.let { onFftData(it) }
            }
        }
        visualizer.setDataCaptureListener(listener, Visualizer.getMaxCaptureRate() / 2, false, true)
    }

    override fun stop() {
        // Listener is managed by the Visualizer instance, which is released in the service.
    }
}

class PollingFftDataProvider(private val coroutineScope: CoroutineScope) : FftDataProvider {
    private var job: Job? = null

    override suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit) {
        val fft = ByteArray(visualizer.captureSize)
        val delayMillis = 40L

        job = coroutineScope.launch {
            while (isActive) {
                if (visualizer.getFft(fft) == Visualizer.SUCCESS) {
                    onFftData(fft)
                }
                delay(delayMillis)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}

