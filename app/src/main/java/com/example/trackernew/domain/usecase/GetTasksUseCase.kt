package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.TasksRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {

    operator fun invoke() = repository.tasks
}