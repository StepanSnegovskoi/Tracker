package com.example.tracker.data.repositoryImpl

import android.app.Application
import com.example.tracker.data.database.CardDatabase
import com.example.tracker.data.mapper.CardMapper
import com.example.tracker.data.mapper.GroupMapper
import com.example.tracker.domain.entity.Card
import com.example.tracker.domain.entity.Group
import com.example.tracker.domain.repository.Repository

class RepositoryImpl(application: Application) : Repository {

    private val cardDao = CardDatabase.getInstance(application).cardDao()
    private val groupDao = CardDatabase.getInstance(application).groupDao()

    override suspend fun addCard(card: Card) {
        cardDao.add(CardMapper.mapCardToCardDbModel(card))
    }

    override suspend fun deleteCard(id: Int) {
        cardDao.delete(id)
    }

    override suspend fun getCard(id: Int): Card {
        return CardMapper.mapCardDbModelToCard(cardDao.getCard(id))
    }

    override suspend fun getAllCardsByGroupName(groupName: String): List<Card> {
        return CardMapper.mapCardDbModelListToCardList(cardDao.getCardsByGroupName(groupName))
    }

    override suspend fun getAllGroups(): List<Group> {
        return GroupMapper.mapGroupDbModelListToGroupList(groupDao.getAllGroups())
    }

    override suspend fun addGroup(group: Group) {
        groupDao.addGroup(GroupMapper.mapGroupToGroupDbModel(group))
    }

    override suspend fun deleteGroup(groupName: String) {
        groupDao.deleteGroup(groupName)
        cardDao.deleteCardsByGroupName(groupName)
    }

    override suspend fun deleteCardsByGroupName(groupName: String) {
        cardDao.deleteCardsByGroupName(groupName)
    }
}
