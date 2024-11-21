package com.example.tracker.domain.logic

import com.example.tracker.domain.repository.Repository

class DeleteGroup(
    private val repository: Repository
) {

    suspend operator fun invoke(groupName: String){
        repository.deleteGroup(groupName)
    }
}
