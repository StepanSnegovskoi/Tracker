package com.example.trackernew.domain.repository

interface DetailsRepository {

    suspend fun deleteLecturer(name: String)

    suspend fun deleteLessonName(name: String)

    suspend fun deleteAudience(name: String)
}