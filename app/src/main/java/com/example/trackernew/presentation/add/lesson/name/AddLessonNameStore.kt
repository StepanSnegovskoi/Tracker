package com.example.trackernew.presentation.add.lesson.name

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.usecase.AddLessonNameUseCase
import com.example.trackernew.presentation.add.lesson.name.AddLessonNameStore.Intent
import com.example.trackernew.presentation.add.lesson.name.AddLessonNameStore.Label
import com.example.trackernew.presentation.add.lesson.name.AddLessonNameStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddLessonNameStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddLessonNameClicked : Intent

        data class ChangeLessonName(val lessonName: String) : Intent
    }

    data class State(
        val lessonName: String
    )

    sealed interface Label {

        data object LessonSaved : Label

        data object AddLessonNameClickedAndNameIsEmpty : Label
    }
}

class AddLessonNameStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addLessonNameUseCase: AddLessonNameUseCase
) {

    fun create(): AddLessonNameStore =
        object : AddLessonNameStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddLecturerStore",
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

        data class ChangeLessonName(val lessonName: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddLessonNameClicked -> {
                    val state = getState()
                    if (state.lessonName.trim().isEmpty()) {
                        publish(Label.AddLessonNameClickedAndNameIsEmpty)
                        return
                    }
                    scope.launch {
                        addLessonNameUseCase(
                            LessonName(
                                name = state.lessonName.trim()
                            )
                        )
                        publish(Label.LessonSaved)
                    }
                }

                is Intent.ChangeLessonName -> {
                    dispatch(Msg.ChangeLessonName(intent.lessonName))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeLessonName -> {
                    copy(lessonName = msg.lessonName)
                }
            }
    }
}
