package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.LessonName
import kotlinx.coroutines.flow.Flow

interface AddLessonNameRepository {

    suspend fun addLessonName(lessonName: LessonName)
}