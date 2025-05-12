package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.WeeksRepository
import javax.inject.Inject

class DeleteWeekByIdUseCase @Inject constructor(
    private val weeksRepository: WeeksRepository
) {

    suspend operator fun invoke(weekId: Int) = weeksRepository.deleteWeek(weekId)
}