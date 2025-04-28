package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.WeekRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeekRepositoryImpl @Inject constructor(
    private val weekDao: WeekDao
) : WeekRepository {

    override val weeks: Flow<List<Week>> = weekDao.getWeeks()
        .map { it.toEntities() }

    override suspend fun addWeek(week: Week) {
        weekDao.addWeek(week.toDbModel())
    }
}