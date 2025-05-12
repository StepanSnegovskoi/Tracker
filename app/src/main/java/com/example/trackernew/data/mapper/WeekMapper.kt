package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.WeekDbModel
import com.example.trackernew.domain.entity.Week

fun Week.toDbModel(): WeekDbModel = WeekDbModel(id, name, isActive, days, selectedAsCurrent, weekOfYear, position)

fun WeekDbModel.toEntity(): Week = Week(id, name, isActive, days, selectedAsCurrent, weekOfYear, position)

fun List<WeekDbModel>.toEntities(): List<Week> = map { it.toEntity() }