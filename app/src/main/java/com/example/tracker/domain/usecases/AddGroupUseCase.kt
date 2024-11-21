package com.example.tracker.domain.usecases

import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class AddGroupUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(group: Group) {
        repository.addGroup(group)
    }
}