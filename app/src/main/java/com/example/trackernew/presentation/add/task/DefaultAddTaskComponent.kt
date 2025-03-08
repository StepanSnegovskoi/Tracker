package com.example.trackernew.presentation.add.task

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

class DefaultAddTaskComponent @AssistedInject constructor(
    private val storeFactory: AddTaskStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("ifCategoriesAreEmpty") ifCategoriesAreEmpty: () -> Unit,
): AddTaskComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            ifCategoriesAreEmpty()
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddTaskStore.State> = store.stateFlow

    override fun onSaveTaskClicked() {
        store.accept(AddTaskStore.Intent.SaveTaskClicked)
    }

    override fun onNameChanged(name: String) {
        store.accept(AddTaskStore.Intent.ChangeName(name))
    }

    override fun onDescriptionChanged(description: String) {
        store.accept(AddTaskStore.Intent.ChangeDescription(description))
    }

    override fun onCategoryChanged(category: String) {
        store.accept(AddTaskStore.Intent.ChangeCategory(category))
    }

    override fun onDeadlineChanged(deadline: Long) {
        store.accept(AddTaskStore.Intent.ChangeDeadline(deadline))
    }

    override fun ifCategoriesAreEmpty() {
        store.accept(AddTaskStore.Intent.CategoriesClickedAndTheyAreEmpty)
    }

    override fun onSubTaskNameChanged(subTask: String) {
        store.accept(AddTaskStore.Intent.ChangeSubTask(subTask))
    }

    override fun onAddSubTaskClicked() {
        store.accept(AddTaskStore.Intent.AddSubTask)
    }

    override fun onDeleteSubTaskClicked(id: Int) {
        store.accept(AddTaskStore.Intent.DeleteSubTask(id))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("ifCategoriesAreEmpty") ifCategoriesAreEmpty: () -> Unit,
        ): DefaultAddTaskComponent
    }
}