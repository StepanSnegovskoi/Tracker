package com.example.trackernew.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorPalette(
    val background: Color,
    val onBackground: Color,
    val textColor: Color,
    val tintColor: Color,
    val oppositeColor: Color = Color.Transparent,
    val averageColor: Color = Color.Transparent
)

val LightColorPalette = ColorPalette(
    background = Color.White,
    onBackground = Color(0xFFB4D2E7),
    textColor = Color.Black,
    tintColor = Color.Black,
    oppositeColor = Color.Black,
)

val DarkColorPalette = ColorPalette(
    background = Color(0xFF333030),
    onBackground = Color(0xFF464444),
    textColor = Color.White,
    tintColor = Color.White,
    oppositeColor = Color.White,
    averageColor = Color(0xFF726F6F)
)

val LocalColors = staticCompositionLocalOf<ColorPalette> {
    error("Colors composition error")
}

@Composable
fun TrackerNewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (!darkTheme) LightColorPalette
    else DarkColorPalette

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

object TrackerNewTheme {
    val colors: ColorPalette
        @Composable @ReadOnlyComposable
        get() = LocalColors.current
}