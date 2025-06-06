package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessonNames")
data class LessonNameDbModel(
    @PrimaryKey
    val name: String
)