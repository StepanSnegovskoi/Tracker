package com.example.tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tracker.data.entity.GroupDbModel

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGroup(group: GroupDbModel)

    @Query("SELECT * FROM groups")
    suspend fun getAllGroups(): List<GroupDbModel>

    @Query("DELETE FROM groups WHERE name = :groupName")
    suspend fun deleteGroup(groupName: String)
}
