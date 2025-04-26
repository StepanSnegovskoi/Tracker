package com.example.trackernew.domain.entity

data class Lesson(
    val id: Int,
    val name: String,
    val time: String,
    val lecturer: String,
    val audience: String,
    val typeOfLesson: TypeOfLesson
)

sealed interface TypeOfLesson {

    data object Practise : TypeOfLesson

    data object Lesson : TypeOfLesson

    data object Another : TypeOfLesson
}