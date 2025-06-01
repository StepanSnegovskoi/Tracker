package com.example.trackernew.domain.usecase

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(category: Category) = repository.deleteCategory(category.name)
}