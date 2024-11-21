package com.example.tracker.domain.usecases

import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class DeleteCardUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Int) {
        repository.deleteCard(id)
    }
}
