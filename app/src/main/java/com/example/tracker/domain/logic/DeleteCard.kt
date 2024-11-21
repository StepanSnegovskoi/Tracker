package com.example.tracker.domain.logic

import com.example.tracker.data.repositoryImpl.RepositoryImpl

class DeleteCard (
    private val cardRepository: RepositoryImpl
) {

    suspend operator fun invoke(id: Int) {
        cardRepository.deleteCard(id)
    }
}
