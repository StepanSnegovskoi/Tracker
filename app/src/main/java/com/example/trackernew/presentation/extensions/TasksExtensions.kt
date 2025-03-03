package com.example.trackernew.presentation.extensions

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

fun Long.toDateString(): String {
    if (this == 0L) return ""

    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        .format(this)
        .toString()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}