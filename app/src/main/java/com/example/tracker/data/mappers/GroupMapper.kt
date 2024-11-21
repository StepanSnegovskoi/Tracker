package com.example.tracker.data.mappers

import com.example.tracker.data.entities.GroupDbModel
import com.example.tracker.domain.entities.Group
import javax.inject.Inject

class GroupMapper @Inject constructor() {
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
