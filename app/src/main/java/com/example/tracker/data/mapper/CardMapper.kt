package com.example.tracker.data.mapper

import com.example.tracker.data.entity.CardDbModel
import com.example.tracker.domain.entity.Card

object CardMapper {
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
