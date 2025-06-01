package com.example.trackernew.presentation.edit.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface EditTaskComponent {

    val model: StateFlow<EditTaskStore.State>

    val labels: Flow<EditTaskStore.Label>


    fun onEditTaskClicked()

    fun onDeleteSubTaskClicked(id: Int)

    fun onAddSubTaskClicked()

    fun onSubTaskChangeStatusClicked(id: Int)

    fun onChangeStatusClicked()

    fun onChangeAlarmEnableClicked()

    fun onChangeTimesCountClicked(timesCount: Int)

    fun onChangeTimeForDeadlineClicked(timeForDeadline: String)


    fun onNameChanged(name: String)

    fun onDescriptionChanged(description: String)

    fun onCategoryChanged(category: String)

    fun onDeadlineChanged(deadline: Long)

    fun onSubTaskNameChanged(subTask: String)
}