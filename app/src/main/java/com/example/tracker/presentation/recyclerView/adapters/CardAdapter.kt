package com.example.tracker.presentation.recyclerView.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tracker.R
import com.example.tracker.domain.entities.Card
import com.example.tracker.presentation.recyclerView.diffCallbacks.CardCallback
import com.example.tracker.presentation.recyclerView.viewHolders.CardViewHolder

class CardAdapter : ListAdapter<Card, CardViewHolder>(
    CardCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        LayoutInflater.from(parent.context).inflate(
            R.layout.card_item,
            parent,
            false
        ).apply {
            return CardViewHolder(this)
        }
    }

    override fun onBindViewHolder(viewHolder: CardViewHolder, position: Int) {
        val card = currentList[position]
        viewHolder.name.text = card.name
        viewHolder.description.text = card.description
        viewHolder.deadline.text = card.deadline.toString()

    }
}