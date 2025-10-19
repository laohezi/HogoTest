package com.example.hogotest.visualizer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun VisualizerScreen(viewModel: VisualizerViewModel = koinViewModel()) {
    val visualizerData by viewModel.visualizerData.collectAsState()
    val processorNames = viewModel.fftProcessorNames

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(visualizerData.color.copy(alpha = visualizerData.alpha)),
    ) {
        Text(
            text = "Visualizer",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                processorNames.forEach { name ->
                    Button(onClick = { viewModel.setFftProcessor(name) }) {
                        Text(text = name)
                    }
                }
            }
        }
    }
}
