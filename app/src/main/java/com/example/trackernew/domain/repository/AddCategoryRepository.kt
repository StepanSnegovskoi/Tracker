package com.example.trackernew.domain.repository

import com.example.trackernew.domain.entity.Category

interface AddCategoryRepository {

    suspend fun addCategory(category: Category)
}