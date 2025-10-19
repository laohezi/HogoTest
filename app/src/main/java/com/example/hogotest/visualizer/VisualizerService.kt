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
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class VisualizerService : Service() {
    private var visualizer: Visualizer? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val visualizerRepository by inject<VisualizerRepository>()

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
            visualizerRepository.stopDataCapture()

            if (sessionId != 0) {
                try {
                    delay(200)
                    visualizer = Visualizer(sessionId).apply {
                        captureSize = Visualizer.getCaptureSizeRange()[1]
                    }

                    Log.i("VisualizerService", "Visualizer setup complete with sessionId: $sessionId")
                    visualizer?.let { viz ->
                        visualizerRepository.startDataCapture(viz)
                        viz.enabled = true
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Error setting up visualizer", e)
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        serviceScope.cancel()
        visualizer?.release()
        visualizerRepository.stopDataCapture()
    }
}
