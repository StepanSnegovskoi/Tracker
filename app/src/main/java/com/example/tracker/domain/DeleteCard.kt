package com.example.tracker.domain

import com.example.tracker.data.CardRepositoryImpl

class DeleteCard (
    private val cardRepository: CardRepositoryImpl
) {

    suspend operator fun invoke(id: Int) {
        cardRepository.deleteCard(id)
    }
}
