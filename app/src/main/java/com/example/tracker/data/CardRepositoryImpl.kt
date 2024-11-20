package com.example.tracker.data

import android.app.Application
import com.example.tracker.domain.Card
import com.example.tracker.domain.CardRepository

class CardRepositoryImpl(application: Application) : CardRepository {

    private val dao = CardDatabase.getInstance(application).dao()

    override suspend fun addCard(card: Card) {
        dao.add(CardMapper.mapCardToCardDbModel(card))
    }

    override suspend fun deleteCard(id: Int) {
        dao.delete(id)
    }

    override suspend fun getCard(id: Int): Card {
        return CardMapper.mapCardDbModelToCard(dao.getCard(id))
    }

    override suspend fun getAllCards(): List<Card> {
        return CardMapper.mapCardDbModelListToCardList(dao.getAllCards())
    }
}