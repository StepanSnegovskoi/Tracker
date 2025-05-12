package com.example.trackernew.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trackernew.domain.entity.SubTask
import com.example.trackernew.domain.entity.TaskStatus

@Entity(tableName = "tasks")
data class TaskDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val status: TaskStatus,
    val addingTime: Long,
    val deadline: Long,
    val subTasks: List<SubTask>
)