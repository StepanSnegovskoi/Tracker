package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.WeekRepository
import javax.inject.Inject

class GetWeeksUseCase @Inject constructor(
    private val weekRepository: WeekRepository
) {

    operator fun invoke() = weekRepository.weeks
}
