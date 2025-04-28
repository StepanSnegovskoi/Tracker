package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.LecturerDbModel
import com.example.trackernew.domain.entity.Lecturer

fun Lecturer.toDbModel(): LecturerDbModel = LecturerDbModel(name)

fun LecturerDbModel.toEntity(): Lecturer = Lecturer(name)

fun List<LecturerDbModel>.toEntities(): List<Lecturer> = map { it.toEntity() }