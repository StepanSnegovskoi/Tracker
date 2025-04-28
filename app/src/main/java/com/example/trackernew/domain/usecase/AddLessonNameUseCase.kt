package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.repository.AddLessonNameRepository
import javax.inject.Inject

class AddLessonNameUseCase @Inject constructor(
    private val addLessonNameRepository: AddLessonNameRepository
) {

    suspend operator fun invoke(lessonName: LessonName) =
        addLessonNameRepository.addLessonName(lessonName)
}
