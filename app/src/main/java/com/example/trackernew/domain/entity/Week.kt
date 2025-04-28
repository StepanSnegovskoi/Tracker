package com.example.trackernew.domain.entity

data class Week(
    val id: Int,
    val name: String,
    val position: Int,
    val isActive: Boolean,
    val days: List<Day> = listOf(
        Day(
            name = "Monday",
            lessons = emptyList()
        ),
        Day(
            name = "Tuesday",
            lessons = emptyList()
        ),
        Day(
            name = "WednesDay",
            lessons = emptyList()
        ),
        Day(
            name = "Thursday",
            lessons = emptyList()
        ),
        Day(
            name = "Friday",
            lessons = emptyList()
        ),
        Day(
            name = "Saturday",
            lessons = emptyList()
        ),
        Day(
            name = "Sunday",
            lessons = emptyList()
        ),
    )
)