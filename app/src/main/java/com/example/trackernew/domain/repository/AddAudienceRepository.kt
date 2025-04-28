package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer

interface AddAudienceRepository {

    suspend fun addAudience(audience: Audience)
}