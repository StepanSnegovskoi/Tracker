package com.example.tracker.domain

import com.example.tracker.data.CardRepositoryImpl

class GetCard(
    private val cardRepository: CardRepositoryImpl
) {

    suspend operator fun invoke(id: Int): Card {
        return cardRepository.getCard(id)
    }
}