package com.example.trackernew.presentation.utils

import com.example.trackernew.domain.entity.Task

sealed class Sort(val value: String) {
    abstract fun comparator(): Comparator<Task>

    data object ByDateAdded : Sort("По дате добавления") {
        override fun comparator() = compareBy<Task> { it.addingTime }
    }

    data object ByDeadline : Sort("По дедлайну") {
        override fun comparator() = compareBy<Task> { it.deadline }
    }

    data object ByName : Sort("По названию") {
        override fun comparator() = compareBy<Task> { it.name }
    }
}

val sortTypes = listOf(Sort.ByDateAdded, Sort.ByDeadline, Sort.ByName)