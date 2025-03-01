package com.example.trackernew.data.repository

import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.AddTaskRepository
import javax.inject.Inject

class AddTaskRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao
) : AddTaskRepository {

    override suspend fun saveTask(task: Task) {
        tasksDao.saveTask(task.toDbModel())
    }
}