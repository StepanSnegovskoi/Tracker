package com.example.tracker.domain.useCases

import com.example.tracker.domain.repository.Repository
import com.example.tracker.domain.entities.Group
import javax.inject.Inject

class GetAllGroupsUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(): List<Group>{
        return repository.getAllGroups()
    }
}