package com.example.tracker.data.mappers

import com.example.tracker.data.entities.CardDbModel
import com.example.tracker.domain.entities.Card
import javax.inject.Inject

class CardMapper @Inject constructor() {
    fun mapCardDbModelToCard(cardDbModel: CardDbModel): Card {
        return Card(
            id = cardDbModel.id,
            name = cardDbModel.name,
            description = cardDbModel.description,
            deadline = cardDbModel.deadline,
            groupName = cardDbModel.groupName,
        )
    }

    fun mapCardToCardDbModel(card: Card): CardDbModel {
        return CardDbModel(
            id = card.id,
            name = card.name,
            description = card.description,
            deadline = card.deadline,
            groupName = card.groupName,
        )
    }

    fun mapCardListToCardDbModelList(cards: List<Card>): List<CardDbModel> {
        return mutableListOf<CardDbModel>().apply {
            cards.forEach {
                add(mapCardToCardDbModel(it))
            }
        }
    }

    fun mapCardDbModelListToCardList(cardsDbModel: List<CardDbModel>): List<Card> {
        return mutableListOf<Card>().apply {
            cardsDbModel.forEach {
                add(mapCardDbModelToCard(it))
            }
        }
    }
}
