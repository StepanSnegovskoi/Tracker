package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.WeeksRepository
import javax.inject.Inject

class EditWeekUseCase @Inject constructor(
    private val weeksRepository: WeeksRepository
) {

    suspend operator fun invoke(week: Week) = weeksRepository.editWeek(week)
}