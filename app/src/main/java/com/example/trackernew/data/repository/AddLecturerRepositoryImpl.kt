package com.example.trackernew.data.repository

import com.example.trackernew.data.db.dao.LessonDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.repository.AddLecturerRepository
import com.example.trackernew.domain.repository.AddLessonNameRepository
import com.example.trackernew.domain.usecase.AddLessonNameUseCase
import javax.inject.Inject

class AddLecturerRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao
): AddLecturerRepository {

    override suspend fun addLecturer(lecturer: Lecturer) {
        lessonDao.addLecturer(lecturer.toDbModel())
    }
}