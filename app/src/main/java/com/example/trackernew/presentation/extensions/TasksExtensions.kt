package com.example.trackernew.presentation.extensions

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.utils.INITIAL_CATEGORY_NAME
import com.example.trackernew.presentation.utils.Sort
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

fun List<Task>.filterBySortTypeAndCategory(sort: Sort, category: Category): List<Task> {
    val sortedTasks = sortedWith(sort.comparator())
    if (category.name == INITIAL_CATEGORY_NAME) {
        return sortedTasks
    }
    return sortedTasks
        .filter { it.category == category.name }
}
