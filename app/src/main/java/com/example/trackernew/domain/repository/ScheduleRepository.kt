package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    val weeks: Flow<List<Week>>

    suspend fun deleteLesson(weekId: Int, lessonId: Int)
}