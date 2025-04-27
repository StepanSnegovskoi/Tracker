package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Lesson

interface AddLessonRepository {

    suspend fun addLesson(lesson: Lesson)
}