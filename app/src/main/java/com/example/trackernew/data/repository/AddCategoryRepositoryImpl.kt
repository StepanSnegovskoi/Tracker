package com.example.trackernew.data.repository

import com.example.trackernew.data.db.CategoryDao
import com.example.trackernew.data.mapper.toDbModel
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.repository.AddCategoryRepository
import javax.inject.Inject

class AddCategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
): AddCategoryRepository {

    override suspend fun addCategory(category: Category) {
        categoryDao.addCategory(category.toDbModel())
    }
}