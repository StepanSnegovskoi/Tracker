package com.example.tracker.domain

interface CardRepository {

    suspend fun addCard(card: Card)

    suspend fun deleteCard(id: Int)

    suspend fun getCard(id: Int): Card

    suspend fun getAllCards(): List<Card>
}
