package com.example.tracker.domain.usecases

import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class DeleteCardsByGroupNameUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(groupName: String){
        repository.deleteCardsByGroupName(groupName)
    }
}
