package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.repository.AddCategoryRepository
import com.example.trackernew.domain.repository.AddTaskRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val addCategoryRepository: AddTaskRepository
) {

    operator fun invoke() = addCategoryRepository.categories
}