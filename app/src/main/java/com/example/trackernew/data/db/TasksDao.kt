package com.example.trackernew.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackernew.data.entity.TaskDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveTask(taskDbModel: TaskDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun editTask(taskDbModel: TaskDbModel)

    @Query("DELETE FROM tasks WHERE id =:id")
    suspend fun deleteTask(id: Int)

    @Query("SELECT * FROM tasks")
    fun getTasks(): Flow<List<TaskDbModel>>
}