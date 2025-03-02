package com.example.trackernew.presentation.add.task

import kotlinx.coroutines.flow.StateFlow

interface AddTaskComponent {

    val model: StateFlow<AddTaskStore.State>

    fun onSaveTaskClicked()

    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onCategoryChanged(category: String)

    fun onDeadlineChanged(deadline: Long)
}