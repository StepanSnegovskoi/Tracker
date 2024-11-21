package com.example.tracker.domain.usecases

import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class AddCardUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(card: Card) {
        repository.addCard(card)
    }
}
