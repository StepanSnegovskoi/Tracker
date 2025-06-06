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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultScheduleComponent @AssistedInject constructor(
    private val storeFactory: ScheduleStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onAddWeekClicked") onAddWeekClicked: () -> Unit,
    @Assisted("onEditWeeksClicked") onEditWeeksClicked: () -> Unit,
    @Assisted("onSettingsClicked") onSettingsClicked: () -> Unit,
    @Assisted("onAddLessonClicked") onAddLessonClicked: (Int, String, Int) -> Unit
) : ScheduleComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    override val labels: Flow<ScheduleStore.Label>
        get() = store.labels

    init {
        store.labels.onEach {
            when (it) {
                ScheduleStore.Label.DaysListIsEmpty -> {
                    /** Nothing **/
                }

                ScheduleStore.Label.AddLessonClickedAndWeeksAreEmpty ->  {
                    /** Nothing **/
                }

                ScheduleStore.Label.ClickAddWeek -> {
                    onAddWeekClicked()
                }

                is ScheduleStore.Label.ClickAddLesson -> {
                    onAddLessonClicked(it.weekId, it.dayName, it.futureLessonId)
                }

                ScheduleStore.Label.ClickEditWeeks -> {
                    onEditWeeksClicked()
                }

                ScheduleStore.Label.ClickSettings -> {
                    onSettingsClicked()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ScheduleStore.State> = store.stateFlow

    override fun onAddWeekClicked() {
        store.accept(ScheduleStore.Intent.ClickAddWeek)
    }

    override fun onAddLessonClicked(weekId: Int, dayName: String, futureLessonId: Int) {
        store.accept(ScheduleStore.Intent.ClickAddLesson(weekId, dayName, futureLessonId))
    }

    override fun onEditWeeksClicked() {
        store.accept(ScheduleStore.Intent.ClickEditWeeks)
    }

    override fun onDeleteLessonClicked(weekId: Int, lessonId: Int) {
        store.accept(ScheduleStore.Intent.ClickDeleteLesson(weekId, lessonId))
    }

    override fun onNavigateToCurrentDayClickedAndDaysListIsEmpty() {
        store.accept(ScheduleStore.Intent.ClickNavigateToCurrentDayAndDaysListIsEmpty)
    }

    override fun onSettingsClicked() {
        store.accept(ScheduleStore.Intent.ClickSettings)
    }

    override fun onAddLessonClickedAndWeeksAreEmpty() {
        store.accept(ScheduleStore.Intent.AddLessonClickedAndWeeksAreEmpty)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAddWeekClicked") onAddWeekClicked: () -> Unit,
            @Assisted("onEditWeeksClicked") onEditWeeksClicked: () -> Unit,
            @Assisted("onSettingsClicked") onSettingsClicked: () -> Unit,
            @Assisted("onAddLessonClicked") onAddLessonClicked: (Int, String, Int) -> Unit
        ): DefaultScheduleComponent
    }
}