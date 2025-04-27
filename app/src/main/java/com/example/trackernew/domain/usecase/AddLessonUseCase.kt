package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.repository.AddLessonRepository
import javax.inject.Inject

class AddLessonUseCase @Inject constructor(
    private val addLessonRepository: AddLessonRepository
) {

    suspend operator fun invoke(lesson: Lesson) = addLessonRepository.addLesson(lesson)
}