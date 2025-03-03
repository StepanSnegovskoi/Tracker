package com.example.trackernew.presentation.tasks

import com.example.trackernew.domain.entity.Task
import kotlinx.coroutines.flow.StateFlow

interface TasksComponent {

    val model: StateFlow<TasksStore.State>

    fun onAddClicked()

    fun onTaskLongClicked(task: Task)
}