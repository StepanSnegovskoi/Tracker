package com.example.tracker.presentation.recyclerView.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tracker.R
import com.example.tracker.domain.entities.Card
import com.example.tracker.presentation.recyclerView.diffCallbacks.CardCallback
import com.example.tracker.presentation.recyclerView.viewHolders.CardViewHolder

class CardAdapter : ListAdapter<Card, CardViewHolder>(
    CardCallback()
) {

    var onCardLongClickListener: ((Card) -> Unit)? = null
        set(value) {
            field = value
        }

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

        with(viewHolder) {
            name.text = card.name
            description.text = card.description
            description.visibility = View.GONE
            deadline.text = card.deadline

            itemView.setOnClickListener {
                when (description.visibility) {
                    View.GONE -> description.visibility = View.VISIBLE
                    else -> description.visibility = View.GONE
                }
            }

            itemView.setOnLongClickListener {
                onCardLongClickListener?.invoke(card)
                true
            }
        }
    }
}
