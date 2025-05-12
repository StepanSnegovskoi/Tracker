package com.example.trackernew.presentation.weeks

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.usecase.DeleteWeekByIdUseCase
import com.example.trackernew.domain.usecase.EditWeekUseCase
import com.example.trackernew.domain.usecase.GetWeeksUseCase
import com.example.trackernew.presentation.weeks.WeeksStore.Intent
import com.example.trackernew.presentation.weeks.WeeksStore.Label
import com.example.trackernew.presentation.weeks.WeeksStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Collections
import javax.inject.Inject

interface WeeksStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeWeekStatus(val week: Week) : Intent

        data object ConfirmEdit : Intent

        data class SelectWeekAsCurrent(val week: Week) : Intent

        data class DeleteWeek(val weekId: Int) : Intent

        data class MoveUpWeek(val week: Week) : Intent

        data class MoveDownWeek(val week: Week) : Intent
    }

    data class State(
        val weeks: List<Week>
    )

    sealed interface Label {

        data object ClickConfirmEditButton : Label
    }
}

class WeeksStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeeksUseCase: GetWeeksUseCase,
    private val editWeekUseCase: EditWeekUseCase,
    private val deleteWeekByIdUseCase: DeleteWeekByIdUseCase
) {

    fun create(): WeeksStore =
        object : WeeksStore, Store<Intent, State, Label> by storeFactory.create(
            name = "WeeksStore",
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

        data class ChangeWeekStatus(val week: Week) : Msg

        data class SelectWeekAsCurrent(val week: Week) : Msg

        data class MoveUpWeek(val week: Week) : Msg

        data class MoveDownWeek(val week: Week) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getWeeksUseCase().onEach {
                dispatch(Action.WeeksLoaded(it
                    .sortedBy { week: Week -> week.position }
                    .sortedBy { week: Week -> !week.isActive }
                ))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeWeekStatus -> {
                    if (intent.week.selectedAsCurrent) return
                    dispatch(Msg.ChangeWeekStatus(intent.week))
                }

                Intent.ConfirmEdit -> {
                    val state = getState()
                    scope.launch {
                        state.weeks.forEachIndexed { index, week ->
                            editWeekUseCase(week.copy(position = index))
                        }
                        publish(Label.ClickConfirmEditButton)
                    }
                }

                is Intent.SelectWeekAsCurrent -> {
                    if (!intent.week.isActive) return
                    dispatch(Msg.SelectWeekAsCurrent(intent.week))
                }

                is Intent.DeleteWeek -> {
                    scope.launch {
                        deleteWeekByIdUseCase(intent.weekId)
                    }
                }

                is Intent.MoveUpWeek -> {
                    dispatch(Msg.MoveUpWeek(intent.week))
                }

                is Intent.MoveDownWeek -> {
                    dispatch(Msg.MoveDownWeek(intent.week))
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

                is Msg.ChangeWeekStatus -> {
                    copy(weeks = buildList {
                        weeks.forEach {
                            if (it.id == msg.week.id) {
                                add(it.copy(isActive = !msg.week.isActive))
                            } else {
                                add(it)
                            }
                        }
                    })
                }

                is Msg.SelectWeekAsCurrent -> {
                    val calendar = Calendar.getInstance()
                    copy(weeks = buildList {
                        weeks.forEach {
                            when (it.id == msg.week.id) {
                                true -> {
                                    add(
                                        it.copy(
                                            selectedAsCurrent = !it.selectedAsCurrent,
                                            weekOfYear = if (!it.selectedAsCurrent) calendar.get(
                                                Calendar.WEEK_OF_YEAR
                                            ) else -1
                                        )
                                    )
                                }

                                false -> {
                                    add(
                                        it.copy(
                                            selectedAsCurrent = false,
                                            weekOfYear = -1
                                        )
                                    )
                                }
                            }
                        }
                    })
                }

                is Msg.MoveUpWeek -> {
                    val weeksList = weeks.toMutableList()
                    val index = weeksList.indexOfFirst { it.id == msg.week.id }

                    Collections.swap(weeksList, index, index - 1)

                    copy(weeks = weeksList)
                }

                is Msg.MoveDownWeek -> {
                    val weeksList = weeks.toMutableList()
                    val index = weeksList.indexOfFirst { it.id == msg.week.id }

                    Collections.swap(weeksList, index, index + 1)

                    copy(weeks = weeksList)
                }
            }
    }
}