package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.LessonNameDbModel
import com.example.trackernew.domain.entity.LessonName

fun LessonName.toDbModel(): LessonNameDbModel = LessonNameDbModel(name)

fun LessonNameDbModel.toEntity(): LessonName = LessonName(name)

fun List<LessonNameDbModel>.toEntities(): List<LessonName> = map { it.toEntity() }