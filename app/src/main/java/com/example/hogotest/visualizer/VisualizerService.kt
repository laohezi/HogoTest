package com.example.hogotest.visualizer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.audiofx.Visualizer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.aidlmodule.IVisualizer
import com.example.hogotest.visualizer.DefaultFFTDataProcessor
import com.example.hogotest.visualizer.FFTDataProcessor
import com.example.hogotest.visualizer.RhythmicFFTDataProcessor
import kotlinx.coroutines.*
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.android.inject

private interface FftDataProvider {
    suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit)
    fun stop()
}

private class ListenerFftDataProvider : FftDataProvider {
    private var listener: Visualizer.OnDataCaptureListener? = null

    override suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit) {
        // It can take a moment for the visualizer to enable.
        // We poll until it's enabled before setting the listener.

        listener = object : Visualizer.OnDataCaptureListener {
            override fun onWaveFormDataCapture(visualizer: Visualizer?, waveform: ByteArray?, samplingRate: Int) {}

            override fun onFftDataCapture(visualizer: Visualizer?, fft: ByteArray?, samplingRate: Int) {
                Log.i("ListenerFftDataProvider", "FFT data captured")

                fft?.let { onFftData(it) }
            }
        }
        visualizer.setDataCaptureListener(listener, Visualizer.getMaxCaptureRate() / 2, false, true)
    }

    override fun stop() {

        // Listener is managed by the Visualizer instance, which is released in the service.
    }
}

private class PollingFftDataProvider(private val coroutineScope: CoroutineScope) : FftDataProvider {
    private var job: Job? = null

    override suspend fun start(visualizer: Visualizer, onFftData: (ByteArray) -> Unit) {
        val fft = ByteArray(visualizer.captureSize)
        val captureRate = Visualizer.getMaxCaptureRate() / 2
     //   val delayMillis = if (captureRate > 0) 1000L / captureRate else 50L
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

class VisualizerService : Service() {
    private var visualizer: Visualizer? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val fftDataProcessorRepository by inject<FftProcessorRepository>()
    private val visualizerRepository by inject<VisualizerRepository>()

    private val usePolling = false // true for polling, false for listener
    private var fftDataProvider: FftDataProvider? = null

    companion object {
        val LOG_TAG = "VisualizerService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "VisualizerServiceChannel"
    }

    private val binder = object : IVisualizer.Stub() {
        override fun updateMediaSessionId(sessionId: Int) {
            Log.i(LOG_TAG, "Received sessionId: $sessionId")
            setupVisualizer(sessionId)
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        startForeground()
        return binder
    }

    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Visualizer Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Visualizer Active")
            .setContentText("Visualizer is running to process audio.")
            .setSmallIcon(android.R.drawable.ic_media_play) // Replace with your app's icon
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun setupVisualizer(sessionId: Int) {
        serviceScope.launch {
            visualizer?.release()
            fftDataProvider?.stop()

            if (sessionId != 0) {
                try {
                    delay(200)
                    visualizer = Visualizer(sessionId).apply {
                        captureSize = Visualizer.getCaptureSizeRange()[1]

                    }

                    fftDataProvider = if (usePolling) {
                        PollingFftDataProvider(serviceScope)
                    } else {
                        ListenerFftDataProvider()
                    }
                    Log.i("VisualizerService", "Visualizer setup complete with sessionId: $sessionId")
                    visualizer?.let { viz ->
                        fftDataProvider?.start(viz) { fft ->
                            processFFTData(fft)

                        }
                        viz.enabled = true
                    }


                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Error setting up visualizer", e)
                    e.printStackTrace()
                }
            }
        }
    }

    private  fun processFFTData(fft: ByteArray) {
        fftDataProcessorRepository.fftProcessor.process(fft)?.let { result ->
            visualizerRepository.updateVisualizerData(result)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        serviceScope.cancel()
        visualizer?.release()
        fftDataProvider?.stop()
    }


}
