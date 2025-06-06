package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.WeekDao
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val weekDao: WeekDao
) : ScheduleRepository {

    override val weeks: Flow<List<Week>> = weekDao.getWeeks().map {
        it.toEntities()
    }

    override suspend fun deleteLesson(weekId: Int, lessonId: Int) {
        weekDao.deleteLessonByLessonId(weekId, lessonId)
    }
}