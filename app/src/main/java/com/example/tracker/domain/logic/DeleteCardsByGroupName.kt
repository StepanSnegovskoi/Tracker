package com.example.tracker.domain.logic

import com.example.tracker.domain.repository.Repository

class DeleteCardsByGroupName(
    private val repository: Repository
) {

    suspend operator fun invoke(groupName: String){
        repository.deleteCardsByGroupName(groupName)
    }
}
