package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.AddTaskRepository
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val addTaskRepository: AddTaskRepository
) {

    suspend operator fun invoke(task: Task) = addTaskRepository.saveTask(task)
}