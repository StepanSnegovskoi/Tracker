package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.data.mapper.toEntities
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.repository.AddLessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddLessonRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
) : AddLessonRepository {

    override suspend fun addLesson(lesson: Lesson) {
        lessonDao.addLesson(lesson.toDbModel())
    }

    override val lessonNames: Flow<List<LessonName>> = lessonDao.getLessonNames()
        .map { it.toEntities() }

    override val lecturers: Flow<List<Lecturer>> = lessonDao.getLecturers()
        .map { it.toEntities() }

    override val audiences: Flow<List<Audience>> = lessonDao.getAudiences()
        .map { it.toEntities() }
}