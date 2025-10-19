package com.example.hogotest.di

import com.example.hogotest.visualizer.VisualizerRepository
import com.example.hogotest.visualizer.VisualizerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val visualizerModule = module {
    single { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    single { VisualizerRepository(get()) }
    viewModel { VisualizerViewModel(get()) }
}
