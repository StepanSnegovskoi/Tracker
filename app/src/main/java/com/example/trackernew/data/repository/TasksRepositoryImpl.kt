package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.CategoryDao
import com.example.trackernew.data.db.dao.TasksDao
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val categoryDao: CategoryDao
): TasksRepository {

    override val categories: Flow<List<Category>> = categoryDao.getCategories()
        .map { it.toEntities() }

    override val tasks: Flow<List<Task>> = tasksDao.getTasks()
        .map { it.toEntities() }
}