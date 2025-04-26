package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackernew.domain.entity.TypeOfLesson

@Entity(tableName = "lessons")
data class LessonDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val time: String,
    val lecturer: String,
    val audience: String,
    val typeOfLesson: TypeOfLesson
)