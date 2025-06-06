package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.DetailsRepository
import javax.inject.Inject

data class DeleteLessonNameUseCase @Inject constructor(
    private val repository: DetailsRepository
) {

    suspend operator fun invoke(name: String) = repository.deleteLessonName(name)
}