package com.example.trackernew.presentation.add.lesson.lecturer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.usecase.AddLecturerUseCase
import com.example.trackernew.presentation.add.lesson.lecturer.AddLecturerStore.Intent
import com.example.trackernew.presentation.add.lesson.lecturer.AddLecturerStore.Label
import com.example.trackernew.presentation.add.lesson.lecturer.AddLecturerStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddLecturerStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddLecturer : Intent

        data class ChangeLecturer(val lecturer: String) : Intent
    }

    data class State(
        val lecturer: String
    )

    sealed interface Label {

        data object LecturerSaved : Label

        data object AddLecturerClickedAndLecturerIsEmpty : Label
    }
}

class AddLecturerStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addLecturerUseCase: AddLecturerUseCase
) {

    fun create(): AddLecturerStore =
        object : AddLecturerStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddLecturerStore",
            initialState = State(
                lecturer = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeLecturer(val lecturer: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddLecturer -> {
                    val state = getState()
                    val lecturer = state.lecturer.trim()

                    when (lecturer.isNotEmpty()) {
                        true -> {
                            scope.launch {
                                addLecturerUseCase(
                                    Lecturer(
                                        name = lecturer
                                    )
                                )
                                publish(Label.LecturerSaved)
                            }
                        }

                        false -> {
                            publish(Label.AddLecturerClickedAndLecturerIsEmpty)
                        }
                    }
                }

                is Intent.ChangeLecturer -> {
                    dispatch(Msg.ChangeLecturer(intent.lecturer))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeLecturer -> {
                    copy(lecturer = msg.lecturer)
                }
            }
    }
}
