package com.example.tracker.domain.logic

import com.example.tracker.data.repositoryImpl.RepositoryImpl
import com.example.tracker.domain.entity.Card

class GetCardsByGroupName (
    private val cardRepository: RepositoryImpl
) {

    suspend operator fun invoke(groupName: String): List<Card> {
        return cardRepository.getAllCardsByGroupName(groupName)
    }
}
