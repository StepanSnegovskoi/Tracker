package com.example.tracker.presentation.recyclerView.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R

class GroupViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val groupName = item.findViewById<TextView>(R.id.textViewGroupName)
}