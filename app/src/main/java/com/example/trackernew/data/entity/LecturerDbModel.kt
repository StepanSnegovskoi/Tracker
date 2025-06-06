package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lecturers")
data class LecturerDbModel(
    @PrimaryKey
    val name: String
)