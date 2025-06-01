package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.domain.repository.DetailsRepository
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : DetailsRepository {
    override suspend fun deleteLecturer(name: String) {
        lessonDao.deleteLecturer(name)
    }

    override suspend fun deleteLessonName(name: String) {
        lessonDao.deleteLessonName(name)
    }

    override suspend fun deleteAudience(name: String) {
        lessonDao.deleteAudience(name)
    }
}