package com.example.tracker.domain.repository

import com.example.tracker.domain.entity.Card
import com.example.tracker.domain.entity.Group

interface Repository {

    suspend fun addCard(card: Card)

    suspend fun deleteCard(id: Int)

    suspend fun getCard(id: Int): Card

    suspend fun getAllCardsByGroupName(groupName: String): List<Card>

    suspend fun addGroup(group: Group)

    suspend fun getAllGroups(): List<Group>
}