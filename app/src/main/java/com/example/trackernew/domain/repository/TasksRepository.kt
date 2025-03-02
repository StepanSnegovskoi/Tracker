package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Task
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    val tasks: Flow<List<Task>>
}