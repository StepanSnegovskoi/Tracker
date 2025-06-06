package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.repository.AddAudienceRepository
import javax.inject.Inject

class AddAudienceUseCase @Inject constructor(
    private val addAudienceRepository: AddAudienceRepository
) {

    suspend operator fun invoke(audience: Audience) = addAudienceRepository.addAudience(audience)
}
