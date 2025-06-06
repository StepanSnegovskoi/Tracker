package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Audience

interface AddAudienceRepository {

    suspend fun addAudience(audience: Audience)
}