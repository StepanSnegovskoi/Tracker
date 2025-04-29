package com.example.trackernew.presentation.schedule

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

class DefaultScheduleComponent @AssistedInject constructor (
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onAddWeekClick") onAddWeekClick: () -> Unit,
    @Assisted("onAddLessonClick") onAddLessonClick: (String, String, String) -> Unit,
    private val storeFactory: ScheduleStoreFactory
): ScheduleComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                ScheduleStore.Label.ClickAddWeek -> {
                    onAddWeekClick()
                }

                is ScheduleStore.Label.ClickAddLesson -> {
                    onAddLessonClick(it.weekId, it.dayName, it.futureLessonId)
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ScheduleStore.State> = store.stateFlow

    override fun onAddWeekButtonClick() {
        store.accept(ScheduleStore.Intent.ClickAddWeek)
    }

    override fun onAddLessonButtonClick(weekId: String, dayName: String, futureLessonId: String) {
        store.accept(ScheduleStore.Intent.ClickAddLesson(weekId, dayName, futureLessonId))
    }

    @AssistedFactory
    interface Factory {

        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAddWeekClick") onAddWeekClick: () -> Unit,
            @Assisted("onAddLessonClick") onAddLessonClick: (String, String, String) -> Unit,
        ): DefaultScheduleComponent
    }
}