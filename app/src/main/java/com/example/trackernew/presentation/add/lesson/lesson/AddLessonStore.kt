package com.example.trackernew.presentation.add.lesson.lesson

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.Lesson
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.entity.TypeOfLesson
import com.example.trackernew.domain.usecase.AddLessonUseCase
import com.example.trackernew.domain.usecase.GetAudiencesUseCase
import com.example.trackernew.domain.usecase.GetLecturersUseCase
import com.example.trackernew.domain.usecase.GetLessonNamesUseCase
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Intent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Label
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddLessonStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object SaveLessonClicked : Intent

        data object NameLessonsClickedAndTheyAreEmpty : Intent

        data class ChangeLessonName(val name: String) : Intent

        data object LecturersClickedAndTheyAreEmpty : Intent

        data class ChangeLecturer(val lecturer: String) : Intent

        data object AudiencesClickedAndTheyAreEmpty : Intent

        data class ChangeAudience(val audience: String) : Intent
    }

    data class State(
        val lessonName: String,
        val lessonNames: List<LessonName>,
        val lecturer: String,
        val lecturers: List<Lecturer>,
        val audience: String,
        val audiences: List<Audience>
    )

    sealed interface Label {

        data object LessonNamesClickedAndTheyAreEmpty : Label

        data object LecturersClickedAndTheyAreEmpty : Label

        data object AudiencesClickedAndTheyAreEmpty : Label

        data object LessonSaved : Label
    }
}

class AddLessonFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val saveLessonUseCase: AddLessonUseCase,
    private val getLessonNamesUseCase: GetLessonNamesUseCase,
    private val getLecturersUseCase: GetLecturersUseCase,
    private val getAudiencesUseCase: GetAudiencesUseCase
) {

    fun create(): AddLessonStore =
        object : AddLessonStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddLessonStore",
            initialState = State(
                lessonName = "",
                lessonNames = emptyList(),
                lecturer = "",
                lecturers = emptyList(),
                audience = "",
                audiences = emptyList(),
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Action

        data class LecturersNamesLoaded(val lecturers: List<Lecturer>) : Action

        data class AudiencesLoaded(val audiences: List<Audience>) : Action
    }

    private sealed interface Msg {

        data class ChangeLessonName(val lessonName: String) : Msg

        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Msg

        data class ChangeLecturer(val lecturer: String) : Msg

        data class LecturersLoaded(val lecturers: List<Lecturer>) : Msg

        data class ChangeAudience(val audience: String) : Msg

        data class AudiencesLoaded(val audiences: List<Audience>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getLessonNamesUseCase().onEach {
                dispatch(Action.LessonNamesLoaded(it))
            }.launchIn(scope)

            getLecturersUseCase().onEach {
                dispatch(Action.LecturersNamesLoaded(it))
            }.launchIn(scope)

            getAudiencesUseCase().onEach {
                dispatch(Action.AudiencesLoaded(it))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.SaveLessonClicked -> {
                    val state = getState()
                    scope.launch {
                        saveLessonUseCase(
                            Lesson(
                                id = 0,
                                name = state.lessonName,
                                start = "",
                                end = "",
                                lecturer = state.lecturer,
                                audience = state.audience,
                                typeOfLesson = TypeOfLesson.Another
                            )
                        )
                        publish(Label.LessonSaved)
                    }
                }

                is Intent.ChangeLessonName -> {
                    dispatch(Msg.ChangeLessonName(intent.name))
                }

                Intent.NameLessonsClickedAndTheyAreEmpty -> {
                    publish(Label.LessonNamesClickedAndTheyAreEmpty)
                }

                is Intent.ChangeLecturer -> {
                    dispatch(Msg.ChangeLecturer(intent.lecturer))
                }

                Intent.LecturersClickedAndTheyAreEmpty -> {
                    publish(Label.LecturersClickedAndTheyAreEmpty)
                }

                is Intent.ChangeAudience -> {
                    dispatch(Msg.ChangeAudience(intent.audience))
                }

                Intent.AudiencesClickedAndTheyAreEmpty -> {
                    publish(Label.AudiencesClickedAndTheyAreEmpty)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LessonNamesLoaded -> {
                    dispatch(Msg.LessonNamesLoaded(action.lessonNames))
                }

                is Action.LecturersNamesLoaded -> {
                    dispatch(Msg.LecturersLoaded(action.lecturers))
                }

                is Action.AudiencesLoaded -> {
                    dispatch(Msg.AudiencesLoaded(action.audiences))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeLessonName -> {
                copy(lessonName = msg.lessonName)
            }

            is Msg.LessonNamesLoaded -> {
                copy(lessonNames = msg.lessonNames)
            }

            is Msg.ChangeLecturer -> {
                copy(lecturer = msg.lecturer)
            }

            is Msg.LecturersLoaded -> {
                copy(lecturers = msg.lecturers)
            }

            is Msg.ChangeAudience -> {
                copy(audience = msg.audience)
            }

            is Msg.AudiencesLoaded -> {
                copy(audiences = msg.audiences)
            }
        }
    }
}