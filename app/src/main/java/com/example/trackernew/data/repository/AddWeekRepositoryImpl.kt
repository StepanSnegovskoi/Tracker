package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.AddWeekRepository
import javax.inject.Inject

class AddWeekRepositoryImpl @Inject constructor(
    private val weekDao: WeekDao
) : AddWeekRepository {

    override suspend fun addWeek(week: Week) {
        weekDao.addWeek(week.toDbModel())
    }
}