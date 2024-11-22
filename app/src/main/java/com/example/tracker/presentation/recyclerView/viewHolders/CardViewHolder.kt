package com.example.tracker.presentation.recyclerView.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.domain.entities.Card
import java.util.Locale

class CardViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val name = item.findViewById<TextView>(R.id.textViewName)
    val description = item.findViewById<TextView>(R.id.textViewDescription)
    val deadline = item.findViewById<TextView>(R.id.textViewDeadline)

}
