package com.example.tracker.data.dbEntity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupDbModel(

    @PrimaryKey
    val name: String
)

