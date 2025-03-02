package com.example.trackernew.presentation.tasks

import kotlinx.coroutines.flow.StateFlow

interface TasksComponent {

    val model: StateFlow<TasksStore.State>

    fun onAddClicked()
}