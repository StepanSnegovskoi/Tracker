package com.example.trackernew.presentation.add.lesson.lesson

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.TypeOfLesson
import com.example.trackernew.domain.usecase.AddLessonUseCase
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Intent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Label
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddLessonStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ChangeName(val name: String) : Intent

        data object SaveLessonClicked : Intent
    }

    data class State(
        val name: String,
        val start: String,
        val end: String,
        val lecturer: String,
        val audience: String,
        val typeOfLesson: TypeOfLesson
    )

    sealed interface Label {
    }
}

class AddLessonStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addLessonUseCase: AddLessonUseCase
) {

    fun create(): AddLessonStore =
        object : AddLessonStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddLessonStore",
            initialState = State(
                name = "",
                start = "",
                end = "",
                lecturer = "",
                audience = "",
                typeOfLesson = TypeOfLesson.Another
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeName(val name: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeName -> {
                    dispatch(Msg.ChangeName(intent.name))
                }

                Intent.SaveLessonClicked -> {
                    val state = getState()
                    scope.launch {
                        addLessonUseCase(
                            lesson = Lesson(
                                id = 0,
                                name = state.name,
                                start = state.start,
                                end = state.end,
                                lecturer = state.lecturer,
                                audience = state.audience,
                                typeOfLesson = state.typeOfLesson
                            )
                        )
                    }

                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeName -> {
                    copy(name = msg.name)
                }
            }
    }
}
