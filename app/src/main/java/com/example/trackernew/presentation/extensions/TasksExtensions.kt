package com.example.trackernew.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.toDateString(): String {
    if (this == 0L) return ""

    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        .format(this)
        .toString()
}