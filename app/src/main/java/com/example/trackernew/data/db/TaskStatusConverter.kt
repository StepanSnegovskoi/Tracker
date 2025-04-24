package com.example.trackernew.data.db

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.SubTask
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskStatusConverter {

    @TypeConverter
    fun fromStatus(status: TaskStatus): String {
        return when (status) {
            is TaskStatus.Executed -> "Executed"
            is TaskStatus.Failed -> "Failed"
            is TaskStatus.Completed -> "Completed"
            is TaskStatus.InTheProcess -> "InTheProcess"
        }
    }

    @TypeConverter
    fun fromJson(statusJson: String): TaskStatus {
        return when (statusJson) {
            "Executed" -> TaskStatus.Executed
            "Failed" -> TaskStatus.Failed
            "Completed" -> TaskStatus.Completed
            "InTheProcess" -> TaskStatus.InTheProcess
            else -> throw IllegalArgumentException("Unknown TaskStatus: $statusJson")
        }
    }
}