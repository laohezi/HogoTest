package com.example.hogotest.visualizer

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt

class RhythmicFFTDataProcessor : FFTDataProcessor {
    // Beat detection for low frequencies (bass/drums)
    private val bassEnergyHistory = mutableListOf<Float>()
    private var lastBassBeatTime = 0L
    private val bassBeatCooldown = 250L // ms
    private val bassHistorySize = 40
    private val bassBeatThresholdMultiplier = 1.4f

    // Beat detection for mid frequencies (melody/vocals)
    private val melodyEnergyHistory = mutableListOf<Float>()
    private var lastMelodyBeatTime = 0L
    private val melodyBeatCooldown = 350L // ms, longer cooldown for less frequent response
    private val melodyHistorySize = 60
    private val melodyBeatThresholdMultiplier = 1.6f

    // Beat brightness variables
    @Volatile
    private var beatBrightness = 0.0f
    private val brightnessDecay = 0.92f // Controls how fast the light fades after a beat
    private val baseBrightness = 0.3f

    override fun process(fft: ByteArray): VisualizerData {
        val magnitudes = FloatArray(fft.size / 2)
        for (i in magnitudes.indices) {
            val real = fft[i * 2].toFloat()
            val imaginary = fft[i * 2 + 1].toFloat()
            magnitudes[i] = sqrt(real * real + imaginary * imaginary)
        }

        // Define frequency bands based on magnitude array indices
        val bassEndIndex = (magnitudes.size * 0.1f).toInt().coerceAtLeast(1) // ~0-10% of spectrum for bass
        val melodyEndIndex = (magnitudes.size * 0.5f).toInt() // ~10-50% for melody

        val bassEnergy = magnitudes.slice(0 until bassEndIndex).average().toFloat()
        val melodyEnergy = magnitudes.slice(bassEndIndex until melodyEndIndex).average().toFloat()

        val isBassBeat = detectBeat(bassEnergy, bassEnergyHistory, bassHistorySize, bassBeatThresholdMultiplier, bassBeatCooldown, ::lastBassBeatTime) { time -> lastBassBeatTime = time }
        val isMelodyBeat = detectBeat(melodyEnergy, melodyEnergyHistory, melodyHistorySize, melodyBeatThresholdMultiplier, melodyBeatCooldown, ::lastMelodyBeatTime) { time -> lastMelodyBeatTime = time }

        val (color, alpha) = calculateVisualResult(magnitudes, isBassBeat || isMelodyBeat)
        return VisualizerData(color, alpha)
    }

    private fun detectBeat(
        currentEnergy: Float,
        energyHistory: MutableList<Float>,
        historySize: Int,
        thresholdMultiplier: Float,
        cooldown: Long,
        getLastBeatTime: () -> Long,
        setLastBeatTime: (Long) -> Unit
    ): Boolean {
        synchronized(energyHistory) {
            if (energyHistory.size > historySize) {
                energyHistory.removeAt(0)
            }
            energyHistory.add(currentEnergy)

            if (energyHistory.size < historySize) {
                return false // Not enough data
            }

            val averageEnergy = energyHistory.average().toFloat()
            val threshold = averageEnergy * thresholdMultiplier

            val currentTime = System.currentTimeMillis()
            if (currentEnergy > threshold && (currentTime - getLastBeatTime()) > cooldown) {
                setLastBeatTime(currentTime)
                return true
            }
            return false
        }
    }

    private fun calculateVisualResult(magnitudes: FloatArray, isBeat: Boolean): Pair<Color, Float> {
        // --- Color Calculation ---
        val lowFreq = magnitudes.take(magnitudes.size / 8).average().toFloat()
        val midFreq = magnitudes.drop(magnitudes.size / 8).take(magnitudes.size / 2).average().toFloat()
        val highFreq = magnitudes.drop(magnitudes.size * 5 / 8).average().toFloat()

        val totalMagnitude = lowFreq + midFreq + highFreq
        if (totalMagnitude == 0f) {
            return Pair(Color.Black, baseBrightness)
        }

        val maxMagnitude = magnitudes.maxOrNull()?.coerceAtLeast(1f) ?: 1f

        val r = (lowFreq / maxMagnitude).pow(0.6f)
        val g = (midFreq / maxMagnitude).pow(0.6f)
        val b = (highFreq / maxMagnitude).pow(0.6f)
        val color = Color(r.coerceIn(0f, 1f), g.coerceIn(0f, 1f), b.coerceIn(0f, 1f))

        // --- Brightness (Alpha) Calculation ---
        if (isBeat) {
            beatBrightness = 1.0f
        } else {
            beatBrightness *= brightnessDecay
        }

        val alpha = (baseBrightness + beatBrightness).coerceIn(0.1f, 1.0f)

        return Pair(color, alpha)
    }
}
