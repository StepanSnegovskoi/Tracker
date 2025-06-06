package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    val categories: Flow<List<Category>>

    val tasks: Flow<List<Task>>

    suspend fun deleteTask(taskId: Int)

    suspend fun deleteCategory(name: String)
}