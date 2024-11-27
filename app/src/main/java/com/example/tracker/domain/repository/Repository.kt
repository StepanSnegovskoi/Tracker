package com.example.tracker.domain.repository

import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.entities.Group

interface Repository {

    suspend fun addCard(card: Card)

    suspend fun deleteCardAndReturnIt(id: Int): Card

    suspend fun getCard(id: Int): Card

    suspend fun getAllCardsByGroupName(groupName: String): List<Card>

    suspend fun deleteCardsByGroupName(groupName: String)

    suspend fun addGroup(group: Group)

    suspend fun getAllGroups(): List<Group>

    suspend fun deleteGroup(groupName: String)
}
