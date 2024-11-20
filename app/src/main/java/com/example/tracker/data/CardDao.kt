package com.example.tracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {

    @Insert
    suspend fun add(cardDbModel: CardDbModel)

    @Query("DELETE FROM cards WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getCard(id: Int): CardDbModel

    @Query("SELECT * FROM cards")
    suspend fun getAllCards(): List<CardDbModel>
}