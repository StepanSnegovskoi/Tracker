package com.example.tracker.data.mapper

import com.example.tracker.data.entity.GroupDbModel
import com.example.tracker.domain.entity.Group

object GroupMapper {
    fun mapGroupDbModelToGroup(cardDbModel: GroupDbModel): Group {
        return Group(
            name = cardDbModel.name
        )
    }

    fun mapGroupToGroupDbModel(card: Group): GroupDbModel {
        return GroupDbModel(
            name = card.name
        )
    }

    fun mapGroupListToGroupDbModelList(cards: List<Group>): List<GroupDbModel> {
        return mutableListOf<GroupDbModel>().apply {
            cards.forEach {
                add(mapGroupToGroupDbModel(it))
            }
        }
    }

    fun mapGroupDbModelListToGroupList(cardsDbModel: List<GroupDbModel>): List<Group> {
        return mutableListOf<Group>().apply {
            cardsDbModel.forEach {
                add(mapGroupDbModelToGroup(it))
            }
        }
    }
}
