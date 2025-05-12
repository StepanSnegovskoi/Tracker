package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.repository.WeeksRepository
import javax.inject.Inject

class UpdateWeekUseCase @Inject constructor(
    private val weeksRepository: WeeksRepository
) {

    suspend operator fun invoke(weekId: Int, dayName: String, lesson: Lesson) =
        weeksRepository.updateWeek(weekId, dayName, lesson)
}