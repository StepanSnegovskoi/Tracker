package com.example.trackernew.data.repository

import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val dao: TasksDao
): TasksRepository {

    override val tasks: Flow<List<Task>> = dao.getTasks()
        .map { it.toEntities() }
}