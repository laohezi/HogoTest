package com.example.hogotest.visualizer

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt



interface FFTDataProcessor {
    fun process(fft: ByteArray): VisualizerData?
}

class DefaultFFTDataProcessor : FFTDataProcessor {
    // Beat detection variables
    private val energyHistory = mutableListOf<Float>()
    private var lastBeatTime = 0L
    private val beatDetectionCooldown = 200 // ms
    private val historySize = 30
    private val beatThresholdMultiplier = 1.5f
    @Volatile
    private var beatBrightness = 0.5f
    private val brightnessDecay = 0.95f

    override fun process(fft: ByteArray): VisualizerData {
        val magnitudes = FloatArray(fft.size / 2)
        for (i in magnitudes.indices) {
            val real = fft[i * 2].toFloat()
            val imaginary = fft[i * 2 + 1].toFloat()
            magnitudes[i] = sqrt(real * real + imaginary * imaginary)
        }

        val lowFreqEnergy = magnitudes.take(magnitudes.size / 3).average().toFloat()
        val isBeat = detectBeat(lowFreqEnergy)

        val avgMagnitude = magnitudes.average().toFloat()
        val (color, alpha) = calculateColorFromFFT(magnitudes, avgMagnitude, isBeat)
        return VisualizerData(color, alpha)
    }

    private fun detectBeat(currentEnergy: Float): Boolean {
        synchronized(energyHistory) {
            if (energyHistory.size > historySize) {
                energyHistory.removeAt(0)
            }
            energyHistory.add(currentEnergy)

            if (energyHistory.size < historySize) {
                return false // Not enough data to make a reliable decision
            }

            val averageEnergy = energyHistory.average().toFloat()
            val threshold = averageEnergy * beatThresholdMultiplier

            val currentTime = System.currentTimeMillis()
            if (currentEnergy > threshold && (currentTime - lastBeatTime) > beatDetectionCooldown) {
                lastBeatTime = currentTime
                return true
            }
            return false
        }
    }

    private fun calculateColorFromFFT(magnitudes: FloatArray, avgMagnitude: Float, isBeat: Boolean): Pair<Color, Float> {
        val lowFreq = magnitudes.take(magnitudes.size / 3).average().toFloat()
        val midFreq = magnitudes.drop(magnitudes.size / 3).take(magnitudes.size / 3).average().toFloat()
        val highFreq = magnitudes.drop(magnitudes.size * 2 / 3).average().toFloat()

        val totalMagnitude = lowFreq + midFreq + highFreq
        if (totalMagnitude == 0f) {
            return Pair(Color.Black, 0.1f)
        }

        val sortedMagnitudes = magnitudes.sorted()
        val maxMagnitude = sortedMagnitudes.getOrElse((sortedMagnitudes.size * 0.95).toInt()) { 1f }.coerceAtLeast(1f)

        val normalizedLow = (lowFreq / maxMagnitude).pow(2)
        val normalizedMid = (midFreq / maxMagnitude).pow(2)
        val normalizedHigh = (highFreq / maxMagnitude).pow(2)

        if (isBeat) {
            beatBrightness = 1.0f
        } else {
            beatBrightness *= brightnessDecay
            beatBrightness = beatBrightness.coerceAtLeast(0.3f)
        }

        val red = (normalizedLow * beatBrightness).coerceIn(0f, 1f)
        val green = (normalizedMid * beatBrightness).coerceIn(0f, 1f)
        val blue = (normalizedHigh * beatBrightness).coerceIn(0f, 1f)

        val dynamicColor = Color(red, green, blue)
        val alpha = (avgMagnitude / maxMagnitude * 2.0f).coerceIn(0.2f, 1f)

        return Pair(dynamicColor, alpha)
    }
}

