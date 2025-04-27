package com.example.trackernew.presentation.add.lesson.lesson

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultAddLessonComponent @AssistedInject constructor(
    private val storeFactory: AddLessonStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : ComponentContext by componentContext, AddLessonComponent {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddLessonStore.State> = store.stateFlow

    override fun onSaveLessonClicked() {
        store.accept(AddLessonStore.Intent.SaveLessonClicked)
    }

    override fun onNameChanged(name: String) {
        store.accept(AddLessonStore.Intent.ChangeName(name))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultAddLessonComponent
    }
}