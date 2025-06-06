package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Lecturer

interface AddLecturerRepository {

    suspend fun addLecturer(lecturer: Lecturer)
}