package com.example.trackernew.presentation.add.week

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.domain.usecase.AddWeekUseCase
import com.example.trackernew.presentation.add.week.AddWeekStore.Intent
import com.example.trackernew.presentation.add.week.AddWeekStore.Label
import com.example.trackernew.presentation.add.week.AddWeekStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddWeekStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddWeekClicked : Intent

        data class ChangeWeek(val week: String) : Intent
    }

    data class State(
        val week: String
    )

    sealed interface Label {

        data object WeekSaved : Label

        data object AddWeekClickedAndNameIsEmpty : Label
    }
}

class AddWeekStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addWeekUseCase: AddWeekUseCase
) {

    fun create(): AddWeekStore =
        object : AddWeekStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddWeekStore",
            initialState = State(
                ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeWeek(val week: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddWeekClicked -> {
                    val state = getState()
                    if (state.week.trim().isEmpty()) {
                        publish(Label.AddWeekClickedAndNameIsEmpty)
                        return
                    }
                    scope.launch {
                        addWeekUseCase(
                            Week(
                                id = 0,
                                name = state.week.trim(),
                                position = 0,
                                isActive = false,
                            )
                        )
                        publish(Label.WeekSaved)
                    }
                }

                is Intent.ChangeWeek -> {
                    dispatch(Msg.ChangeWeek(intent.week))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeWeek -> {
                    copy(week = msg.week)
                }
            }
    }
}
