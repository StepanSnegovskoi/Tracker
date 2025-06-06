package com.example.trackernew.data.mapper

import com.example.trackernew.data.entity.AudienceDbModel
import com.example.trackernew.domain.entity.Audience

fun Audience.toDbModel(): AudienceDbModel = AudienceDbModel(name)

fun AudienceDbModel.toEntity(): Audience = Audience(name)

fun List<AudienceDbModel>.toEntities(): List<Audience> = map { it.toEntity() }