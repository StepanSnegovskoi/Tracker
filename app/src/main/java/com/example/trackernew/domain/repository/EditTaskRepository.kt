package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Task

interface EditTaskRepository {

    suspend fun editTask(task: Task)
}