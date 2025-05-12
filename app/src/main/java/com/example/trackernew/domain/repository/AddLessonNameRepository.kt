package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.LessonName

interface AddLessonNameRepository {

    suspend fun addLessonName(lessonName: LessonName)
}