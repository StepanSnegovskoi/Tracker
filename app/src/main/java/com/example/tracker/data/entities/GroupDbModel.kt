package com.example.tracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupDbModel(

    @PrimaryKey
    val name: String
)

