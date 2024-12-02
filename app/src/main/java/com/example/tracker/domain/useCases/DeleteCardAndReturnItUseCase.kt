package com.example.tracker.domain.useCases

import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class DeleteCardAndReturnItUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(id: Int): Card {
        return repository.deleteCardAndReturnIt(id)
    }
}
