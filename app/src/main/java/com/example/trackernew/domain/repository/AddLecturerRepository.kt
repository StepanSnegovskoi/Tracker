package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.LessonName
import kotlinx.coroutines.flow.Flow

interface AddLecturerRepository {

    suspend fun addLecturer(lecturer: Lecturer)
}