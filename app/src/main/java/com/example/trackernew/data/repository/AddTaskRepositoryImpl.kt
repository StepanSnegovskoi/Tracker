package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.CategoryDao
import com.example.trackernew.data.db.dao.TasksDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.AddTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddTaskRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val categoryDao: CategoryDao
) : AddTaskRepository {

    override suspend fun saveTask(task: Task) {
        tasksDao.saveTask(task.toDbModel())
    }

    override val categories: Flow<List<Category>> = categoryDao.getCategories()
        .map { it.toEntities() }
}