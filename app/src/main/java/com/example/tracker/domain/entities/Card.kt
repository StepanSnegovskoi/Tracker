package com.example.tracker.domain.entities

data class Card(
    val id: Int = 0,
    val name: String,
    val description: String,
    val deadline: Int,
    val groupName: String,
)
