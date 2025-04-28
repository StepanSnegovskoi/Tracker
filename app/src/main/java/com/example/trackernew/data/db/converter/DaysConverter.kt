package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.Day
import com.example.trackernew.domain.entity.SubTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DaysConverter {

    @TypeConverter
    fun fromSubTasks(days: List<Day>): String {
        return Gson().toJson(days)
    }

    @TypeConverter
    fun fromJson(daysJson: String): List<Day> {
        if (daysJson.isBlank()) return emptyList()
        val listType = object : TypeToken<List<Day>>() {}.type
        return Gson().fromJson(daysJson, listType)
    }
}