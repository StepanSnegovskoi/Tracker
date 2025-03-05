package com.example.trackernew.domain.usecase

import com.example.trackernew.data.db.TasksDao
import javax.inject.Inject

class DeleteTaskByIdUseCase @Inject constructor(
    private val tasksDao: TasksDao
) {

    suspend operator fun invoke(id: Int) = tasksDao.deleteTask(id)
}