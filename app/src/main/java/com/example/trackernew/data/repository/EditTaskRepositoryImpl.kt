package com.example.trackernew.data.repository

import com.example.trackernew.data.db.TasksDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.EditTaskRepository
import javax.inject.Inject

class EditTaskRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao
) : EditTaskRepository {

    override suspend fun editTask(task: Task) {
        tasksDao.editTask(task.toDbModel())
    }
}