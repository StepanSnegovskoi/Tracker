package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.repository.AddCategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val addCategoryRepository: AddCategoryRepository
) {

    suspend operator fun invoke(category: Category) = addCategoryRepository.addCategory(category)
}