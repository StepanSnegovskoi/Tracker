package com.example.trackernew.presentation.tasks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.presentation.extensions.componentScope
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
    @Assisted("onAddClick") private val onAddClick: () -> Unit,
    @Assisted("onTaskLongClick") private val onTaskLongClick: () -> Unit
) : TasksComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { tasksStoreFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                TasksStore.Label.ClickAdd -> {
                    onAddClick()
                }
                TasksStore.Label.LongClickTask -> {
                    onTaskLongClick()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<TasksStore.State> = store.stateFlow

    override fun onAddClicked() {
        store.accept(TasksStore.Intent.ClickAdd)
    }

    override fun onTaskLongClicked() {
        store.accept(TasksStore.Intent.LongClickTask)
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAddClick") onAddClick: () -> Unit,
            @Assisted("onTaskLongClick") onTaskLongClick: () -> Unit,
        ): DefaultTasksComponent
    }
}