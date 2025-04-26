package com.example.trackernew.domain.entity

data class Lesson(
    val id: Int,
    val name: String,
    val time: String,
    val lecturer: String,
    val audience: String,
    val typeOfLesson: TypeOfLesson
)

sealed class TypeOfLesson(val value: String) {
    data object Lecture : TypeOfLesson("Лекция")
    data object Practise : TypeOfLesson("Практика")
    data object Another : TypeOfLesson("Другое")
}