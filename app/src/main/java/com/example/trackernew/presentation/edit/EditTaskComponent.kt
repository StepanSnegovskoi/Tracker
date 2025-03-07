package com.example.trackernew.presentation.edit

import kotlinx.coroutines.flow.StateFlow

interface EditTaskComponent {

    val model: StateFlow<EditTaskStore.State>

    fun onEditTaskClicked()

    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onCategoryChanged(category: String)

    fun onDeadlineChanged(deadline: Long)

    fun onChangeCompletedStatusClick()
}