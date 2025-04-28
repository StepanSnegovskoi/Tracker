package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackernew.domain.entity.Day

@Entity(tableName = "weeks")
data class WeekDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val position: Int,
    val isActive: Boolean,
    val days: List<Day> = listOf(
        Day(
            name = "Monday",
            lessons = emptyList()
        ),
        Day(
            name = "Tuesday",
            lessons = emptyList()
        ),
        Day(
            name = "WednesDay",
            lessons = emptyList()
        ),
        Day(
            name = "Thursday",
            lessons = emptyList()
        ),
        Day(
            name = "Friday",
            lessons = emptyList()
        ),
        Day(
            name = "Saturday",
            lessons = emptyList()
        ),
        Day(
            name = "Sunday",
            lessons = emptyList()
        ),
    )
)