package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.LessonDbModel
import com.example.trackernew.domain.entity.Lesson

fun Lesson.toDbModel(): LessonDbModel = LessonDbModel(id, name, start, end, lecturer, audience, typeOfLesson)

fun LessonDbModel.toEntity(): Lesson = Lesson(id, name, start, end, lecturer, audience, typeOfLesson)

fun List<LessonDbModel>.toEntities(): List<Lesson> = map { it.toEntity() }