package com.example.trackernew.presentation.settings

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultScheduleSettingsComponent @AssistedInject constructor(
    private val storeFactory: ScheduleSettingsStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
) : ComponentContext by componentContext, ScheduleSettingsComponent{

    val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ScheduleSettingsStore.State> = store.stateFlow

    override fun onDeleteLecturerClicked(name: String) {
        store.accept(ScheduleSettingsStore.Intent.ClickDeleteLecturer(name))
    }

    override fun onDeleteLessonNameClicked(name: String) {
        store.accept(ScheduleSettingsStore.Intent.ClickDeleteLessonName(name))
    }

    override fun onDeleteAudienceClicked(name: String) {
        store.accept(ScheduleSettingsStore.Intent.ClickDeleteAudience(name))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultScheduleSettingsComponent
    }
}