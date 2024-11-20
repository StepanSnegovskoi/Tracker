package com.example.tracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tracker.data.dbEntity.CardDbModel

@Dao
interface CardDao {

    @Insert
    suspend fun add(cardDbModel: CardDbModel)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCard(id: Int): CardDbModel

    @Query("SELECT * FROM cards WHERE cards.groupName = :groupName")
    suspend fun getCardsByGroupName(groupName: String): List<CardDbModel>

    @Query("DELETE FROM cards WHERE id < 100")
    suspend fun clear()
}
