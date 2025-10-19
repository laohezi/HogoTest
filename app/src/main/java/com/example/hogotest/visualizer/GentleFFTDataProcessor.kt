package com.example.hogotest.visualizer

import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class GentleFFTDataProcessor : FFTDataProcessor {
    // Beat detection variables
    private val energyHistory = mutableListOf<Float>()
    private var lastBeatTime = 0L
    private val beatDetectionCooldown = 200 // ms
    private val historySize = 30
    private val beatThresholdMultiplier = 1.5f

    // Breathing effect variables
    private var phase = 0.0
    private val breathingSpeed = 0.05 // Controls the speed of the breathing effect
    private val breathingAmplitude = 0.1f // Controls the intensity of the breathing effect

    // Beat brightness variables
    @Volatile
    private var beatBrightness = 0.0f
    private val brightnessDecay = 0.90f // Slower decay for a smoother effect
    private val baseBrightness = 0.4f

    override fun process(fft: ByteArray): VisualizerData {
        val magnitudes = FloatArray(fft.size / 2)
        for (i in magnitudes.indices) {
            val real = fft[i * 2].toFloat()
            val imaginary = fft[i * 2 + 1].toFloat()
            magnitudes[i] = sqrt(real * real + imaginary * imaginary)
        }

        val lowFreqEnergy = magnitudes.take(magnitudes.size / 3).average().toFloat()
        val isBeat = detectBeat(lowFreqEnergy)

        val (color, alpha) = calculateVisualResult(magnitudes, isBeat)
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

    private fun calculateVisualResult(magnitudes: FloatArray, isBeat: Boolean): Pair<Color, Float> {
        // --- Color Calculation ---
        val lowFreq = magnitudes.take(magnitudes.size / 3).average().toFloat()
        val midFreq = magnitudes.drop(magnitudes.size / 3).take(magnitudes.size / 3).average().toFloat()
        val highFreq = magnitudes.drop(magnitudes.size * 2 / 3).average().toFloat()

        val totalMagnitude = lowFreq + midFreq + highFreq
        if (totalMagnitude == 0f) {
            return Pair(Color.Black, 0.1f)
        }

        val maxMagnitude = magnitudes.maxOrNull()?.coerceAtLeast(1f) ?: 1f

        val r = (lowFreq / maxMagnitude).pow(0.5f)
        val g = (midFreq / maxMagnitude).pow(0.5f)
        val b = (highFreq / maxMagnitude).pow(0.5f)
        val color = Color(r.coerceIn(0f, 1f), g.coerceIn(0f, 1f), b.coerceIn(0f, 1f))

        // --- Brightness (Alpha) Calculation ---
        if (isBeat) {
            beatBrightness = 1.0f
        } else {
            beatBrightness *= brightnessDecay
        }

        // Breathing effect
        phase += breathingSpeed
        if (phase > 2 * PI) {
            phase -= 2 * PI
        }
        val breathingEffect = (cos(phase).toFloat() * breathingAmplitude) + baseBrightness

        // Combine beat brightness with breathing effect
        val alpha = (breathingEffect + beatBrightness).coerceIn(0.1f, 1.0f)

        return Pair(color, alpha)
    }
}

