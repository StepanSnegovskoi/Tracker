package com.example.tracker.domain.useCases

import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(groupName: String){
        repository.deleteGroup(groupName)
    }
}
