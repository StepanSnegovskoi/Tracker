package com.example.tracker.data.repositoryimpl

import com.example.tracker.data.dao.CardDao
import com.example.tracker.data.dao.GroupDao
import com.example.tracker.data.mappers.CardMapper
import com.example.tracker.data.mappers.GroupMapper
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val cardMapper: CardMapper,
    private val groupMapper: GroupMapper,
    private val cardDao: CardDao,
    private val groupDao: GroupDao
) : Repository {

    override suspend fun addCard(card: Card) {
        cardDao.add(cardMapper.mapCardToCardDbModel(card))
    }

    override suspend fun deleteCardAndReturnIt(id: Int): Card {
        getCard(id).let {
            cardDao.delete(id)
            return it
        }
    }

    override suspend fun getCard(id: Int): Card {
        return cardMapper.mapCardDbModelToCard(cardDao.getCard(id))
    }

    override suspend fun getAllCardsByGroupName(groupName: String): List<Card> {
        return cardMapper.mapCardDbModelListToCardList(cardDao.getCardsByGroupName(groupName))
    }

    override suspend fun getAllGroups(): List<Group> {
        return groupMapper.mapGroupDbModelListToGroupList(groupDao.getAllGroups())
    }

    override suspend fun addGroup(group: Group) {
        groupDao.addGroup(groupMapper.mapGroupToGroupDbModel(group))
    }

    override suspend fun deleteGroup(groupName: String) {
        groupDao.deleteGroup(groupName)
        cardDao.deleteCardsByGroupName(groupName)
    }

    override suspend fun deleteCardsByGroupName(groupName: String) {
        cardDao.deleteCardsByGroupName(groupName)
    }
}
