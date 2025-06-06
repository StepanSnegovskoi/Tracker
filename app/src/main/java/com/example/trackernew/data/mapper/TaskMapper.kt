package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.TaskDbModel
import com.example.trackernew.domain.entity.Task

fun Task.toDbModel(): TaskDbModel = TaskDbModel(id, name, description, category, status, addingTime, deadline, subTasks, alarmEnable, timeUnit, timeUnitCount)

fun TaskDbModel.toEntity(): Task = Task(id, name, description, category, status, addingTime, deadline, subTasks, alarmEnable, timeUnit, timesCount)

fun List<TaskDbModel>.toEntities(): List<Task> = map { it.toEntity() }