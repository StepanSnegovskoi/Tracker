package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.Flow

interface WeeksRepository {

    val weeks: Flow<List<Week>>

    suspend fun updateWeek(weekId: Int, dayName: String, lesson: Lesson)

    suspend fun editWeek(week: Week)

    suspend fun deleteWeek(weekId: Int)
}