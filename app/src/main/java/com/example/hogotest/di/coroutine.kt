package com.example.hogotest.di

import kotlinx.coroutines.Dispatchers

val ioScope by lazy { kotlinx.coroutines.CoroutineScope(Dispatchers.IO) }
val mainScope by lazy { kotlinx.coroutines.CoroutineScope(Dispatchers.Main) }
val defaultScope by lazy { kotlinx.coroutines.CoroutineScope(Dispatchers.Default) }
val unconfinedScope by lazy { kotlinx.coroutines.CoroutineScope(Dispatchers.Unconfined) }
//                 val number = inputText.toIntOrNull()