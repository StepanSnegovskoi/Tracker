package com.example.tracker.domain.usecases

import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class GetCardUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Int): Card {
        return repository.getCard(id)
    }
}