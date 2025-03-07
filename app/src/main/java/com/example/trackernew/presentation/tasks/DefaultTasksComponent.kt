package com.example.trackernew.presentation.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.extensions.componentScope
import com.example.trackernew.presentation.utils.Sort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultTasksComponent @AssistedInject constructor(
    private val tasksStoreFactory: TasksStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onAddTaskClick") private val onAddTaskClick: () -> Unit,
    @Assisted("onAddCategoryClick") private val onAddCategoryClick: () -> Unit,
    @Assisted("onTaskLongClick") private val onTaskLongClick: (Task) -> Unit
) : TasksComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { tasksStoreFactory.create() }

    init {
        store.labels.onEach {
            when(val label = it){
                TasksStore.Label.ClickAddTask -> {
                    onAddTaskClick()
                }
                is TasksStore.Label.LongClickTask -> {
                    onTaskLongClick(label.task)
                }

                TasksStore.Label.ClickAddCategory -> {
                    onAddCategoryClick()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TasksStore.State> = store.stateFlow

    override fun onAddTaskClicked() {
        store.accept(TasksStore.Intent.ClickAddTask)
    }

    override fun onTaskLongClicked(task: Task) {
        store.accept(TasksStore.Intent.LongClickTask(task))
    }

    override fun onSortChanged(sort: Sort) {
        store.accept(TasksStore.Intent.ChangeSort(sort))
    }

    override fun onCategoryChanged(category: Category) {
        store.accept(TasksStore.Intent.ChangeCategory(category))
    }

    override fun onDeleteTaskClicked(task: Task) {
        store.accept(TasksStore.Intent.ClickDeleteTask(task))
    }

    override fun onAddCategoryClicked() {
        store.accept(TasksStore.Intent.ClickAddCategory)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAddTaskClick") onAddTaskClick: () -> Unit,
            @Assisted("onTaskLongClick") onTaskLongClick: (Task) -> Unit,
            @Assisted("onAddCategoryClick") onAddCategoryClick: () -> Unit,
        ): DefaultTasksComponent
    }
}