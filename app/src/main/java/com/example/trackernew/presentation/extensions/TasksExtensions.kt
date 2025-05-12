package com.example.trackernew.presentation.extensions

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.tasks.Sort
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

fun Long.toDateString(): String {
    if (this == 0L) return "00:00"

    return SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        .format(this)
        .toString()
}

fun Long.toTimeString(): String {
    if (this == 0L) return "00:00"

    return SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(this)
        .toString()
}

fun String.toTimeString(): String {
    return if(this == "") "00:00" else toLong().toTimeString()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}

fun List<Task>.filterBySortTypeAndCategory(sort: Sort, category: Category): List<Task> {
    val sortedTasks = sortedWith(sort.comparator())
    if (category.name == "Всё вместе") {
        return sortedTasks
    }
    return sortedTasks
        .filter { it.category == category.name }
}
