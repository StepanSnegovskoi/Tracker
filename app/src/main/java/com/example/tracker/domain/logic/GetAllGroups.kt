package com.example.tracker.domain.logic

import com.example.tracker.domain.repository.Repository
import com.example.tracker.domain.entity.Group

class GetAllGroups(
    private val repository: Repository
) {

    suspend operator fun invoke(): List<Group>{
        return repository.getAllGroups()
    }
}