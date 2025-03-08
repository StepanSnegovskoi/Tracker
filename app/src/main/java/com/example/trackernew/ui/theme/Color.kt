package com.example.trackernew.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledBorderColor = Color.Black.copy(0.3f),
    focusedBorderColor = Color.Black.copy(0.3f),
    unfocusedBorderColor = Color.Black.copy(0.3f),
    disabledTextColor = Color.Black,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    disabledLabelColor = Color.Black.copy(0.7f),
    focusedLabelColor = Color.Black.copy(0.7f),
    unfocusedLabelColor = Color.Black.copy(0.7f),
)

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Green = Color(0xFF198D03)
val Red = Color(0xFFFF4141)