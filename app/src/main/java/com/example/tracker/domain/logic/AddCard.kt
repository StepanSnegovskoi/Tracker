package com.example.tracker.domain.logic

import com.example.tracker.data.repositoryImpl.RepositoryImpl
import com.example.tracker.domain.entity.Card

class AddCard(
    private val cardRepository: RepositoryImpl
) {

    suspend operator fun invoke(card: Card) {
        cardRepository.addCard(card)
    }
}
