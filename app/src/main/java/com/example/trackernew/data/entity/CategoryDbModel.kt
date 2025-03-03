package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
class CategoryDbModel(
    @PrimaryKey
    val name: String
)