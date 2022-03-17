package com.example.app1.utils

import androidx.compose.ui.graphics.Color

fun hexToColor(hex: String?, fallBack: Color = Color.Transparent): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        fallBack
    }
}

