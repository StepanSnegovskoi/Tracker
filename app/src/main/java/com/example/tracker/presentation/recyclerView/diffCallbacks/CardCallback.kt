package com.example.tracker.presentation.recyclerView.diffCallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.tracker.domain.entities.Card

class CardCallback : DiffUtil.ItemCallback<Card>() {

    override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
        return oldItem == newItem
    }
}