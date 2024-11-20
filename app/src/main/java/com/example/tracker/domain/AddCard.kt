package com.example.tracker.domain

import com.example.tracker.data.CardRepositoryImpl

class AddCard(
    private val cardRepository: CardRepositoryImpl
) {

    suspend operator fun invoke(card: Card) {
        cardRepository.addCard(card)
    }
}
