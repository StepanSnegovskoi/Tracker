package com.example.trackernew.presentation.add.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddTaskComponent {

    val model: StateFlow<AddTaskStore.State>

    val labels: Flow<AddTaskStore.Label>

    fun onAddTaskClicked()

    fun onAddSubTaskClicked()

    fun onDeleteSubTaskClicked(id: Int)


    fun onCategoryClickedAndCategoriesListIsEmpty()


    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onCategoryChanged(category: String)

    fun onDeadlineChanged(deadline: Long)

    fun onSubTaskNameChanged(subTask: String)
}