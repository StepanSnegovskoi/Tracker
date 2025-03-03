package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import kotlinx.coroutines.flow.Flow

interface AddTaskRepository {

    val categories: Flow<List<Category>>

    suspend fun saveTask(task: Task)
}