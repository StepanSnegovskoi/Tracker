package com.example.trackernew.presentation.schedule

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.usecase.GetWeeksUseCase
import com.example.trackernew.presentation.schedule.ScheduleStore.Intent
import com.example.trackernew.presentation.schedule.ScheduleStore.Label
import com.example.trackernew.presentation.schedule.ScheduleStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface ScheduleStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickAddWeek : Intent

        data object ClickAddLesson : Intent
    }

    data class State(
        val weeks: List<Week>,
    )

    sealed interface Label {

        data object ClickAddWeek : Label

        data object ClickAddLesson : Label
    }
}

class ScheduleStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getWeeksUseCase: GetWeeksUseCase
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
        override fun invoke() {
            getWeeksUseCase().onEach {
                dispatch(Action.WeeksLoaded(it))
            }.launchIn(scope)
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                Intent.ClickAddWeek -> {
                    publish(Label.ClickAddWeek)
                }

                Intent.ClickAddLesson -> {
                    publish(Label.ClickAddLesson)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when(action){
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
