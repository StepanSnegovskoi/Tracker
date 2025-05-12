package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.AddWeekRepository
import javax.inject.Inject

class AddWeekUseCase @Inject constructor(
    private val weekRepository: AddWeekRepository
) {

    suspend operator fun invoke(week: Week) = weekRepository.addWeek(week)
}
