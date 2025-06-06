package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class LessonDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val start: Long,
    val end: Long,
    val lecturer: String,
    val audience: String,
    val typeOfLesson: String
)