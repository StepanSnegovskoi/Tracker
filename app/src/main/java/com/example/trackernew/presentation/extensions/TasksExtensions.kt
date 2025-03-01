package com.example.trackernew.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.toDateString(): String {
    return SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(this).toString()
}