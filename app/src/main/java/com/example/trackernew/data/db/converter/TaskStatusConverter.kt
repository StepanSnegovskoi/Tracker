package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.TaskStatus

class TaskStatusConverter {

    @TypeConverter
    fun fromStatus(status: TaskStatus): String {
        return when (status) {
            TaskStatus.Executed -> "Executed"
            TaskStatus.Failed -> "Failed"
            TaskStatus.Completed -> "Completed"
            TaskStatus.InTheProcess -> "InTheProcess"
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