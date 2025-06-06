package com.example.trackernew.ui.theme

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun getOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledBorderColor = TrackerNewTheme.colors.oppositeColor,
    focusedBorderColor = TrackerNewTheme.colors.oppositeColor,
    unfocusedBorderColor = TrackerNewTheme.colors.oppositeColor,
    errorBorderColor = TrackerNewTheme.colors.oppositeColor,
    disabledTextColor = TrackerNewTheme.colors.textColor,
    focusedTextColor = TrackerNewTheme.colors.textColor,
    unfocusedTextColor = TrackerNewTheme.colors.textColor,
    cursorColor = TrackerNewTheme.colors.textColor,
    focusedLabelColor = TrackerNewTheme.colors.textColor,
    unfocusedLabelColor = TrackerNewTheme.colors.textColor,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getDatePickerColors() = DatePickerDefaults.colors().copy(
    containerColor = TrackerNewTheme.colors.background,
    titleContentColor = TrackerNewTheme.colors.textColor,
    headlineContentColor = TrackerNewTheme.colors.textColor,
    weekdayContentColor = TrackerNewTheme.colors.textColor,
    navigationContentColor = TrackerNewTheme.colors.textColor,
    dayContentColor = TrackerNewTheme.colors.textColor,
    yearContentColor = TrackerNewTheme.colors.textColor,
    dateTextFieldColors = getOutlinedTextFieldColors()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getTimePickerColors() = TimePickerDefaults.colors().copy(
    clockDialColor = TrackerNewTheme.colors.onBackground,
    timeSelectorSelectedContainerColor = TrackerNewTheme.colors.averageColor,
    timeSelectorUnselectedContainerColor = TrackerNewTheme.colors.onBackground,
    timeSelectorSelectedContentColor = TrackerNewTheme.colors.textColor,
    timeSelectorUnselectedContentColor = TrackerNewTheme.colors.textColor,
    clockDialSelectedContentColor = TrackerNewTheme.colors.textColor,
    clockDialUnselectedContentColor = TrackerNewTheme.colors.textColor,
)

val Green400 = Color(0xFF145B0C)
val Green300 = Color(0xFF196710)
val Green200 = Color(0xFF15720B)
val Green100 = Color(0xFF217A17)

val Red400 = Color(0xFF810C0C)
val Red300 = Color(0xFFFF0000)
val Red200 = Color(0xFFFF5555)

val Gray100 = Color(0xFF726F6F)

val Orange100 = Color(0xFFfc9003)

val Yellow100 = Color.Yellow

val Black500 = Color(0xFF020202)
val Black400 = Color(0xFF090909)
val Black300 = Color(0xFF0d0d0d)
val Black200 = Color(0xFF111111)
val Black100 = Color(0xFF181818)

val White300 = Color(0xFFeeeedd)
val White200 = Color(0xFFffffee)
val White100 = Color.White

val linearGradientLightBackgroundBrush = Brush.linearGradient(colors = listOf(
    White300,
    White300,
    White300,
    White300,
    White300,
    White100,
    White100,
    White300,
))

val linearGradientDarkBackgroundBrush = Brush.linearGradient(colors = listOf(
    Black300,
    Black500,
    Black200,
    Black500,
    Black400,
    Black100,
    Black100,
    Black400,
))
