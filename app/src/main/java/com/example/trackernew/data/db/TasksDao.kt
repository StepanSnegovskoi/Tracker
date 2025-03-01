package com.example.trackernew.data.db

import androidx.room.Dao
import androidx.room.Insert
import com.example.trackernew.data.entity.TaskDbModel

@Dao
interface TasksDao {

    @Insert
    suspend fun saveTask(taskDbModel: TaskDbModel)
}