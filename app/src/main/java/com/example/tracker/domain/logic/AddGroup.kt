package com.example.tracker.domain.logic

import com.example.tracker.domain.entity.Group
import com.example.tracker.domain.repository.Repository

class AddGroup(
    private val repository: Repository
) {

    suspend operator fun invoke(group: Group) {
        repository.addGroup(group)
    }
}