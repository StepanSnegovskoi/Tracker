package com.example.tracker.presentation.recyclerView.diffCallbacks

import androidx.recyclerview.widget.DiffUtil
import com.example.tracker.domain.entities.Group

class GroupDiffCallback : DiffUtil.ItemCallback<Group>() {

    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem == newItem
    }
}