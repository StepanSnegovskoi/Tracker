package com.example.trackernew.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val status: TaskStatus,
    val addingTime: Long,
    val deadline: Long,
    val subTasks: List<SubTask>,
    val alarmEnable: Boolean,
    val timeUnit: String,
    val timeUnitCount: Int
)

sealed interface TaskStatus {

    data object Executed : TaskStatus

    data object Failed : TaskStatus

    data object Completed : TaskStatus

    data object InTheProcess : TaskStatus
}