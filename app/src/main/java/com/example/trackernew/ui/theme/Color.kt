package com.example.trackernew.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledBorderColor = TrackerNewTheme.colors.oppositeColor,
    focusedBorderColor = TrackerNewTheme.colors.onBackground,
    unfocusedBorderColor = TrackerNewTheme.colors.onBackground,
    errorBorderColor = TrackerNewTheme.colors.onBackground,
    disabledTextColor = TrackerNewTheme.colors.textColor,
    focusedTextColor = TrackerNewTheme.colors.textColor,
    unfocusedTextColor = TrackerNewTheme.colors.textColor,
    cursorColor = TrackerNewTheme.colors.textColor,
    focusedLabelColor = TrackerNewTheme.colors.textColor,
    unfocusedLabelColor = TrackerNewTheme.colors.textColor,
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Green = Color(0xFF198D03)
val Red = Color(0xFFFF4141)