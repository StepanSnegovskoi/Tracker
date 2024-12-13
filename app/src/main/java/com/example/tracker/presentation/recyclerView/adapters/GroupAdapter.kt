package com.example.tracker.presentation.recyclerView.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tracker.R
import com.example.tracker.domain.entities.Group
import com.example.tracker.presentation.recyclerView.diffCallbacks.GroupDiffCallback
import com.example.tracker.presentation.recyclerView.viewHolders.GroupViewHolder

class GroupAdapter : ListAdapter<Group, GroupViewHolder>(
    GroupDiffCallback()
) {

    var onGroupClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {

        LayoutInflater.from(parent.context).inflate(
            R.layout.group_item,
            parent,
            false
        ).apply {
            return GroupViewHolder(this)
        }
    }

    override fun onBindViewHolder(viewHolder: GroupViewHolder, position: Int) {
        val groupName = viewHolder.groupName
        groupName.text = currentList[position].name

        groupName.setOnClickListener {
            onGroupClick?.invoke(currentList[position].name)
        }
    }
}
