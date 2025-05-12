package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteTaskByIdUseCase @Inject constructor(
    private val repository: TasksRepository
) {

    suspend operator fun invoke(id: Int) = repository.deleteTask(id)
}