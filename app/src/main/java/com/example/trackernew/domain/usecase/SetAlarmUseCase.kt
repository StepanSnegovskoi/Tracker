package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.repository.AlarmManagerRepository
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(
    private val alarmManagerRepository: AlarmManagerRepository
) {
    
    operator fun invoke(task: Task, time: Long, id: Int) = alarmManagerRepository.setAlarm(task, time, id)
}