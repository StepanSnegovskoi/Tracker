package com.example.trackernew.ui.theme

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledBorderColor = TrackerNewTheme.colors.onBackground,
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

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Green = Color(0xFF198D03)
val Red = Color(0xFFFF4141)