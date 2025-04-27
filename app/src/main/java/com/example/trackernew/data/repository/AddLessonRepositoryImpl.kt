package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.repository.AddLessonRepository
import javax.inject.Inject

class AddLessonRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : AddLessonRepository {

    override suspend fun addLesson(lesson: Lesson) {
        lessonDao.addLesson(lesson.toDbModel())
    }
}