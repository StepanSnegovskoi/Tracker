package com.example.tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tracker.data.entity.CardDbModel

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(cardDbModel: CardDbModel)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM cards WHERE groupName = :groupName")
    suspend fun deleteCardsByGroupName(groupName: String)

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCard(id: Int): CardDbModel

    @Query("SELECT * FROM cards WHERE groupName = :groupName")
    suspend fun getCardsByGroupName(groupName: String): List<CardDbModel>

    @Query("DELETE FROM cards WHERE id < 1000")
    suspend fun clear()
}
