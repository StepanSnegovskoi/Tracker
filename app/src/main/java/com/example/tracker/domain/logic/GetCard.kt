package com.example.tracker.domain.logic

import com.example.tracker.data.repositoryImpl.RepositoryImpl
import com.example.tracker.domain.entity.Card

class GetCard(
    private val cardRepository: RepositoryImpl
) {

    suspend operator fun invoke(id: Int): Card {
        return cardRepository.getCard(id)
    }
}