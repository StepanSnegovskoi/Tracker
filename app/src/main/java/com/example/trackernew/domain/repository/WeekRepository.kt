package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.Flow

interface WeekRepository {

    val weeks: Flow<List<Week>>

    suspend fun addWeek(week: Week)
}