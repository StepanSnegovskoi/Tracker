package com.example.trackernew.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class ColorPalette(
    val background: Color,
    val onBackground: Color,
    val textColor: Color,
    val tintColor: Color,
    val oppositeColor: Color = Color.Transparent,
    val averageColor: Color = Color.Transparent,
    val linearGradientBackground: Brush,
    val darkGreen: Color,
    val lightGreen: Color,
    val red: Color
)

val LightColorPalette = ColorPalette(
    background = White300,
    onBackground = White200,
    textColor = Black300,
    tintColor = Black100,
    oppositeColor = Black300,
    linearGradientBackground = linearGradientLightBackgroundBrush,
    darkGreen = Green200,
    lightGreen = Green100,
    red = Red200,
)

val DarkColorPalette = ColorPalette(
    background = Black300,
    onBackground = Black100,
    textColor = White300,
    tintColor = White300,
    oppositeColor = White200,
    averageColor = Gray100,
    linearGradientBackground = linearGradientDarkBackgroundBrush,
    darkGreen = Green400,
    lightGreen = Green300,
    red = Red400
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