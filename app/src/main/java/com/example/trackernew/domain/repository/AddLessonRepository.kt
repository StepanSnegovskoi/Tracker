package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.LessonName
import kotlinx.coroutines.flow.Flow

interface AddLessonRepository {

    val lessonNames: Flow<List<LessonName>>

    val lecturers: Flow<List<Lecturer>>

    val audiences: Flow<List<Audience>>

    suspend fun addLesson(lesson: Lesson)
}