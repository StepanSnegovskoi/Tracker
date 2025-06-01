package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackernew.data.entity.CategoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryDbModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategory(category: CategoryDbModel)

    @Query("DELETE FROM categories WHERE name =:name")
    suspend fun deleteCategory(name: String)
}