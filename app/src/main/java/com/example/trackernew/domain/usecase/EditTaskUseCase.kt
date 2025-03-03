package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.EditTaskRepository
import javax.inject.Inject

class EditTaskUseCase @Inject constructor(
    private val editTaskRepository: EditTaskRepository
) {

    suspend operator fun invoke(task: Task) = editTaskRepository.editTask(task)
}