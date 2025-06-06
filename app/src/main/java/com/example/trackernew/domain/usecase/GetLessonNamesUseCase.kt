package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.AddLessonRepository
import javax.inject.Inject

class GetLessonNamesUseCase @Inject constructor(
    private val addLessonRepository: AddLessonRepository
) {

    operator fun invoke() = addLessonRepository.lessonNames
}
