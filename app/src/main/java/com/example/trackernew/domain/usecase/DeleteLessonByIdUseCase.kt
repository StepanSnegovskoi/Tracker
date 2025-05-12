package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.ScheduleRepository
import javax.inject.Inject

data class DeleteLessonByIdUseCase @Inject constructor(
    private val repository: ScheduleRepository
) {

    suspend operator fun invoke(weekId: Int, lessonId: Int) = repository.deleteLesson(weekId, lessonId)
}