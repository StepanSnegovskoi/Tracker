package com.example.tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tracker.data.dbEntity.GroupDbModel

@Dao
interface GroupDao {

    @Insert
    suspend fun addGroup(group: GroupDbModel)

    @Query("SELECT * FROM groups")
    suspend fun getAllGroups(): List<GroupDbModel>
}
