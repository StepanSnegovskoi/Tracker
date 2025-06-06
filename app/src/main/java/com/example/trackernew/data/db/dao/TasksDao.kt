package com.example.trackernew.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackernew.data.entity.TaskDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTask(taskDbModel: TaskDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editTask(taskDbModel: TaskDbModel)

    @Query("DELETE FROM tasks WHERE id =:id")
    suspend fun deleteTaskById(id: Int)

    @Query("DELETE FROM tasks WHERE category =:category")
    suspend fun deleteTaskByCategory(category: String)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskDbModel>>
}