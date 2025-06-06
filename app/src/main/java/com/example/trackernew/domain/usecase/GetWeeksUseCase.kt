package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.WeeksRepository
import javax.inject.Inject

class GetWeeksUseCase @Inject constructor(
    private val weeksRepository: WeeksRepository
) {

    operator fun invoke() = weeksRepository.weeks
}
