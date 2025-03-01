package com.example.trackernew.data.entity

import java.util.Calendar

data class TaskDbModel(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val isCompleted: Boolean,
    val addingTime: Calendar,
    val deadline: Calendar
)