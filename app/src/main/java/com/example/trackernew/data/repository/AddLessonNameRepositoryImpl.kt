package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.repository.AddLessonNameRepository
import javax.inject.Inject

class AddLessonNameRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
): AddLessonNameRepository {

    override suspend fun addLessonName(lessonName: LessonName) {
        lessonDao.addLessonName(lessonName.toDbModel())
    }
}