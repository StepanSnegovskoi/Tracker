package com.example.trackernew.presentation.add.task

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultAddTaskComponent @AssistedInject constructor(
    private val storeFactory: AddTaskStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext
): AddTaskComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

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

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddTaskComponent
    }
}