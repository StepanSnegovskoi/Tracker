package com.example.trackernew.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SubTask(
    val id: Int,
    val name: String,
    val isCompleted: Boolean
)