package com.example.trackernew.presentation.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DefaultEditTaskComponent @AssistedInject constructor(
    private val storeFactory: EditTaskStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("task") task: Task,
    @Assisted("onTaskEdited") onTaskEdited: () -> Unit,
): EditTaskComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(task) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<EditTaskStore.State> = store.stateFlow

    init {
        store.labels.onEach {
            when(val label = it){
                EditTaskStore.Label.TaskEdited -> {
                    onTaskEdited()
                }
            }
        }.launchIn(componentScope())
    }

    override fun onEditTaskClicked() {
        store.accept(EditTaskStore.Intent.EditTaskClicked)
    }

    override fun onNameChanged(name: String) {
        store.accept(EditTaskStore.Intent.ChangeName(name))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(EditTaskStore.Intent.ChangeDescription(description))
    }

    override fun onCategoryChanged(category: String) {
        store.accept(EditTaskStore.Intent.ChangeCategory(category))
    }

    override fun onDeadlineChanged(deadline: Long) {
        store.accept(EditTaskStore.Intent.ChangeDeadline(deadline))
    }

    override fun onChangeCompletedStatusClick() {
        store.accept(EditTaskStore.Intent.ChangeCompletedStatusClicked)
    }

    override fun onSubTaskNameChanged(subTask: String) {
        store.accept(EditTaskStore.Intent.ChangeSubTask(subTask))
    }

    override fun onAddSubTaskClicked() {
        store.accept(EditTaskStore.Intent.AddSubTask)
    }

    override fun onDeleteSubTaskClicked(id: Int) {
        store.accept(EditTaskStore.Intent.DeleteSubTaskClicked(id))
    }

    override fun onSubTaskChangeStatusClicked(id: Int) {
        store.accept(EditTaskStore.Intent.ChangeSubTaskStatusClicked(id))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("task") task: Task,
            @Assisted("onTaskEdited") onTaskEdited: () -> Unit,
        ): DefaultEditTaskComponent
    }
}