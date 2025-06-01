package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.AlarmManagerRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(
    private val alarmManagerRepository: AlarmManagerRepository
) {

    operator fun invoke(id: Int) = alarmManagerRepository.cancelAlarm(id)
}