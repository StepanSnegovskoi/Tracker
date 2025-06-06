package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.SubTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SubTasksConverter {

    @TypeConverter
    fun fromSubTasks(subTasks: List<SubTask>): String {
        return Gson().toJson(subTasks)
    }

    @TypeConverter
    fun fromJson(subtasksJson: String): List<SubTask> {
        if (subtasksJson.isBlank()) return emptyList()
        val listType = object : TypeToken<List<SubTask>>() {}.type
        return Gson().fromJson(subtasksJson, listType)
    }
}