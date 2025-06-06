package com.example.trackernew.data.db.converter

import androidx.room.TypeConverter
import com.example.trackernew.domain.entity.TaskStatus

private const val COMPLETED = "Завершён"
private const val EXECUTED = "Выполнен"
private const val FAILED = "Провален"
private const val IN_THE_PROCESS = "В процессе"

class TaskStatusConverter {

    @TypeConverter
    fun fromStatus(status: TaskStatus): String {
        return when (status) {
            TaskStatus.Executed -> EXECUTED
            TaskStatus.Failed -> FAILED
            TaskStatus.Completed -> COMPLETED
            TaskStatus.InTheProcess -> IN_THE_PROCESS
        }
    }

    @TypeConverter
    fun fromJson(statusJson: String): TaskStatus {
        return when (statusJson) {
            EXECUTED -> TaskStatus.Executed
            FAILED -> TaskStatus.Failed
            COMPLETED -> TaskStatus.Completed
            IN_THE_PROCESS -> TaskStatus.InTheProcess
            else -> throw IllegalArgumentException("Неизвестный TaskStatus: $statusJson")
        }
    }
}