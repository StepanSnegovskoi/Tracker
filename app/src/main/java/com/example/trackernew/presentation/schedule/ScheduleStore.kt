package com.example.trackernew.presentation.schedule

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.usecase.DeleteLessonByIdUseCase
import com.example.trackernew.domain.usecase.GetWeeksUseCase
import com.example.trackernew.presentation.schedule.ScheduleStore.Intent
import com.example.trackernew.presentation.schedule.ScheduleStore.Label
import com.example.trackernew.presentation.schedule.ScheduleStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

interface ScheduleStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickAddWeek : Intent

        data object ClickEditWeeks : Intent

        data class ClickDeleteLesson(val weekId: Int, val lessonId: Int) : Intent

        data class ClickAddLesson(
            val weekId: Int,
            val dayName: String,
            val futureLessonId: Int
        ) : Intent
    }

    data class State(
        val weeks: List<Week>
    )

    sealed interface Label {

        data object ClickAddWeek : Label

        data object ClickEditWeeks : Label

        data class ClickAddLesson(
            val weekId: Int,
            val dayName: String,
            val futureLessonId: Int
        ) : Label
    }
}

class ScheduleStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeeksUseCase: GetWeeksUseCase,
    private val deleteLessonByIdUseCase: DeleteLessonByIdUseCase,
) {

    fun create(): ScheduleStore =
        object : ScheduleStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ScheduleStore",
            initialState = State(
                weeks = emptyList()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class WeeksLoaded(val weeks: List<Week>) : Action
    }

    private sealed interface Msg {

        data class WeeksLoaded(val weeks: List<Week>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        private val calendar = Calendar.getInstance()
        override fun invoke() {
            getWeeksUseCase().onEach { weeks ->
                when(weeks.isNotEmpty() && weeks.any { it.selectedAsCurrent }){
                    true -> {
                        val activeWeeks = weeks.filter { it.isActive }.sortedBy { it.position }
                        val currentWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)

                        val currentWeekIndex = activeWeeks.indexOfFirst {
                            it.weekOfYear > -1
                        }

                        val selectedWeekOfYear = activeWeeks.maxOf { it.weekOfYear }

                        val tab =
                            (currentWeekOfYear - selectedWeekOfYear + currentWeekIndex) % activeWeeks.size

                        val schedule = if (tab == 0) {
                            buildList {
                                addAll(activeWeeks.subList(tab + 1, activeWeeks.size))
                                add(activeWeeks[tab])
                                addAll(activeWeeks.subList(tab + 1, activeWeeks.size))
                            }
                        } else if (tab == activeWeeks.size - 1) {
                            buildList {
                                addAll(activeWeeks.subList(0, tab))
                                add(activeWeeks[tab])
                                addAll(activeWeeks.subList(0, tab))
                            }
                        } else {
                            buildList {
                                addAll(activeWeeks.subList(tab + 1, activeWeeks.size))
                                addAll(activeWeeks.subList(0, tab))
                                add(activeWeeks[tab])
                                addAll(activeWeeks.subList(tab + 1, activeWeeks.size))
                                addAll(activeWeeks.subList(0, tab))
                            }
                        }
                        dispatch(Action.WeeksLoaded(schedule))
                    }
                    false -> {
                        dispatch(Action.WeeksLoaded(emptyList()))
                    }
                }
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAddWeek -> {
                    publish(Label.ClickAddWeek)
                }

                is Intent.ClickAddLesson -> {
                    publish(
                        Label.ClickAddLesson(
                            weekId = intent.weekId,
                            dayName = intent.dayName,
                            futureLessonId = intent.futureLessonId
                        )
                    )
                }

                Intent.ClickEditWeeks -> {
                    publish(Label.ClickEditWeeks)
                }

                is Intent.ClickDeleteLesson -> {
                    scope.launch {
                        deleteLessonByIdUseCase(intent.weekId, intent.lessonId)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.WeeksLoaded -> {
                    dispatch(Msg.WeeksLoaded(action.weeks))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.WeeksLoaded -> {
                    copy(weeks = msg.weeks)
                }
            }
    }
}
