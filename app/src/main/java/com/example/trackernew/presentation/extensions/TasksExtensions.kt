package com.example.trackernew.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.toDateString(): String {
    return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(this.time).toString()
}