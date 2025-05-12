package com.example.trackernew.presentation.add.lesson.lecturer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultAddLecturerComponent @AssistedInject constructor (
    private val storeFactory: AddLecturerStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onLecturerSaved") onLecturerSaved: () -> Unit
): AddLecturerComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                AddLecturerStore.Label.AddLecturerClickedAndLecturerIsEmpty -> {
                    /** Nothing **/
                }

                AddLecturerStore.Label.LecturerSaved -> {
                    onLecturerSaved()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddLecturerStore.State> = store.stateFlow

    override val labels: Flow<AddLecturerStore.Label> = store.labels

    override fun onAddLecturerClicked() {
        store.accept(AddLecturerStore.Intent.AddLecturer)
    }

    override fun onLecturerChanged(lecturer: String) {
        store.accept(AddLecturerStore.Intent.ChangeLecturer(lecturer))
    }

    @AssistedFactory
    interface Factory {
        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onLecturerSaved") onLecturerSaved: () -> Unit
        ): DefaultAddLecturerComponent
    }
}