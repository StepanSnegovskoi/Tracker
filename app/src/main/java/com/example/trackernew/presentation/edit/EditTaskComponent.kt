package com.example.trackernew.presentation.edit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface EditTaskComponent {

    val model: StateFlow<EditTaskStore.State>

    val labels: Flow<EditTaskStore.Label>

    fun onEditTaskClicked()

    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onCategoryChanged(category: String)

    fun onDeadlineChanged(deadline: Long)

    fun onChangeCompletedStatusClick()

    fun onSubTaskNameChanged(subTask: String)

    fun onAddSubTaskClicked()

    fun onDeleteSubTaskClicked(id: Int)

    fun onSubTaskChangeStatusClicked(id: Int)
}