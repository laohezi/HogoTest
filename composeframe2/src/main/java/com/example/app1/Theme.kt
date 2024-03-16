package com.example.app1

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyTheme(
    drak: Boolean = isSystemInDarkTheme(),
    content: Content
) {
    MaterialTheme(
        colors = if (drak){
            DarkThemeColors
                          }else{
                              LightThemesColors
                          },
        content = content
    )
}


val LightThemesColors = lightColors(
    primary = Color(0xff53C1BE),
    primaryVariant = Color(0xff53C1BE),
    secondary = Color(0xff53C1BE),
    secondaryVariant = Color(0xff53C1BE),
    error = Color(0xff53C1BE)
)

val DarkThemeColors = darkColors(

)

typealias Content = @Composable () -> Unit