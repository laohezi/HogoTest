package com.example.hogotest.di

import com.example.hogotest.visualizer.FftProcessorRepository
import com.example.hogotest.visualizer.VisualizerRepository
import com.example.hogotest.visualizer.VisualizerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val visualizerModule = module {

    single { VisualizerRepository() }
    single { FftProcessorRepository() }
    viewModel { VisualizerViewModel(get(), get()) }
}
