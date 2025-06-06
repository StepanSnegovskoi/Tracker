package com.example.trackernew.presentation.add.lesson.name

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

class DefaultAddLessonNameComponent @AssistedInject constructor (
    private val storeFactory: AddLessonNameStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onLessonNameSaved") onLessonNameSaved: () -> Unit,
): AddLessonNameComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                AddLessonNameStore.Label.AddLessonNameClickedAndNameIsEmpty -> {
                    /** Nothing **/
                }

                AddLessonNameStore.Label.LessonNameSaved -> {
                    onLessonNameSaved()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddLessonNameStore.State> = store.stateFlow

    override val labels: Flow<AddLessonNameStore.Label> = store.labels

    override fun onAddLessonNameClicked() {
        store.accept(AddLessonNameStore.Intent.AddLessonName)
    }

    override fun onLessonNameChanged(lessonName: String) {
        store.accept(AddLessonNameStore.Intent.ChangeLessonName(lessonName))
    }

    override fun onClearLessonNameClicked() {
        store.accept(AddLessonNameStore.Intent.ChangeLessonName(""))
    }

    @AssistedFactory
    interface Factory {

        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onLessonNameSaved") onLessonNameSaved: () -> Unit
        ): DefaultAddLessonNameComponent
    }
}