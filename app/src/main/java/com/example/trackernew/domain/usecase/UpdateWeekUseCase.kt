package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.WeekRepository
import javax.inject.Inject

class UpdateWeekUseCase @Inject constructor(
    private val weekRepository: WeekRepository
) {

    suspend operator fun invoke(weekId: String, dayName: String, lesson: Lesson) =
        weekRepository.updateWeek(weekId, dayName, lesson)
}