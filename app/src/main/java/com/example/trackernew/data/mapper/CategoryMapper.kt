package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.CategoryDbModel
import com.example.trackernew.domain.entity.Category

fun Category.toDbModel(): CategoryDbModel = CategoryDbModel(name)

fun CategoryDbModel.toEntity(): Category = Category(name)

fun List<CategoryDbModel>.toEntities(): List<Category> = map { it.toEntity() }