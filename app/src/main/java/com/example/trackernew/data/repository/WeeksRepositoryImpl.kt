package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.WeeksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeeksRepositoryImpl @Inject constructor(
    private val weekDao: WeekDao
) : WeeksRepository {

    override val weeks: Flow<List<Week>> = weekDao.getWeeks()
        .map { it.toEntities() }

    override suspend fun updateWeek(weekId: Int, dayName: String, lesson: Lesson) {
        weekDao.addLessonByWeekIdAndDayName(weekId, dayName, lesson.toDbModel())
    }

    override suspend fun editWeek(week: Week) {
        weekDao.updateWeek(week.toDbModel())
    }

    override suspend fun deleteWeek(weekId: Int) {
        weekDao.deleteWeek(weekId)
    }
}