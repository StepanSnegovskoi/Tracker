package com.example.tracker.domain

import com.example.tracker.data.CardRepositoryImpl

class GetAllCards (
    private val cardRepository: CardRepositoryImpl
) {

    suspend operator fun invoke(): List<Card> {
        return cardRepository.getAllCards()
    }
}
