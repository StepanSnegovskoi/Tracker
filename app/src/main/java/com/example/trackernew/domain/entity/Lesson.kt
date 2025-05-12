package com.example.trackernew.domain.entity

data class Lesson(
    val id: Int,
    val name: String,
    val start: Long,
    val end: Long,
    val lecturer: String,
    val audience: String,
    val typeOfLesson: String,
)

val lessons = listOf(
    "Practise",
    "Lesson",
    "Another"
)
