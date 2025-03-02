package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Task

interface AddTaskRepository {

    suspend fun saveTask(task: Task)
}