package com.example.trackernew.domain.entity

import java.util.Calendar

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val isCompleted: Boolean,
    val addingTime: Long,
    val deadline: Long
)