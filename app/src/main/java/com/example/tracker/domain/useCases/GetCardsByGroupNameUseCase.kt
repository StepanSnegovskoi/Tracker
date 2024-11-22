package com.example.tracker.domain.useCases

import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class GetCardsByGroupNameUseCase @Inject constructor (
    private val repository: Repository
) {

    suspend operator fun invoke(groupName: String): List<Card> {
        return repository.getAllCardsByGroupName(groupName)
    }
}
