package com.example.trackernew.presentation.tasks

import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.utils.Sort
import kotlinx.coroutines.flow.StateFlow

interface TasksComponent {

    val model: StateFlow<TasksStore.State>

    fun onAddTaskClicked()

    fun onTaskLongClicked(task: Task)

    fun onSortChanged(sort: Sort)

    fun onCategoryChanged(category: Category)

    fun onDeleteTaskClicked(task: Task)

    fun onAddCategoryClicked()

    fun onScheduleClicked()
}