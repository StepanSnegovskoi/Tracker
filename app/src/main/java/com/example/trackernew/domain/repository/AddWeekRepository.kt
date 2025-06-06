package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Week

interface AddWeekRepository {

    suspend fun addWeek(week: Week)
}