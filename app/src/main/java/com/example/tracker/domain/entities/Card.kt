package com.example.tracker.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Card(
    val id: Int = 0,
    val name: String,
    val description: String,
    val deadline: String,
    val groupName: String,
) : Parcelable
