package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audiences")
data class AudienceDbModel(
    @PrimaryKey
    val name: String
)