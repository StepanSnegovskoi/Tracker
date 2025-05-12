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
import com.example.trackernew.domain.usecase.GetAudiencesUseCase
import com.example.trackernew.domain.usecase.GetLecturersUseCase
import com.example.trackernew.domain.usecase.GetLessonNamesUseCase
import com.example.trackernew.domain.usecase.UpdateWeekUseCase
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Intent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.Label
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddLessonStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object SaveLesson : Intent


        data object NameLessonsListIsEmpty : Intent

        data object LecturersListIsEmpty : Intent

        data object AudiencesListIsEmpty : Intent


        data class ChangeLessonName(val name: String) : Intent

        data class ChangeLecturer(val lecturer: String) : Intent

        data class ChangeAudience(val audience: String) : Intent

        data class ChangeStart(val start: Long) : Intent

        data class ChangeEnd(val end: Long) : Intent

        data class ChangeTypeOfLesson(val typeOfLesson: String) : Intent
    }

    data class State(
        val weekId: Int,
        val futureLessonId: Int,
        val dayName: String,

        val lessonName: String,
        val lecturer: String,
        val audience: String,
        val start: Long,
        val end: Long,
        val typeOfLesson: String,

        val lessonNames: List<LessonName>,
        val lecturers: List<Lecturer>,
        val audiences: List<Audience>,
    )

    sealed interface Label {

        data object LessonSaved : Label


        data object NameLessonsListIsEmpty : Label

        data object LecturersListIsEmpty : Label

        data object AudiencesListIsEmpty : Label


        data object AddLessonClickedAndLessonNameIsEmpty : Label

        data object EndTimeSaveClickedAndItsLessThanStartTime : Label

        data object StartTimeSaveClickedAndItsMoreThanEndTime : Label
    }
}

class AddLessonFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val updateWeekUseCase: UpdateWeekUseCase,
    private val getLessonNamesUseCase: GetLessonNamesUseCase,
    private val getLecturersUseCase: GetLecturersUseCase,
    private val getAudiencesUseCase: GetAudiencesUseCase
) {

    fun create(weekId: Int, dayName: String, futureLessonId: Int): AddLessonStore =
        object : AddLessonStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddLessonStore",
            initialState = State(
                weekId = weekId,
                futureLessonId = futureLessonId,
                dayName = dayName,
                lessonName = "",
                lecturer = "",
                audience = "",
                start = 0L,
                end = 0L,
                typeOfLesson = "Another",
                lessonNames = emptyList(),
                lecturers = emptyList(),
                audiences = emptyList()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Action

        data class LecturerNamesLoaded(val lecturers: List<Lecturer>) : Action

        data class AudiencesLoaded(val audiences: List<Audience>) : Action
    }

    private sealed interface Msg {

        data class ChangeLessonName(val lessonName: String) : Msg

        data class ChangeLecturer(val lecturer: String) : Msg

        data class ChangeAudience(val audience: String) : Msg

        data class ChangeStart(val start: Long) : Msg

        data class ChangeEnd(val end: Long) : Msg

        data class ChangeTypeOfLesson(val typeOfLesson: String) : Msg


        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Msg

        data class LecturersLoaded(val lecturers: List<Lecturer>) : Msg

        data class AudiencesLoaded(val audiences: List<Audience>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getLessonNamesUseCase().onEach {
                dispatch(Action.LessonNamesLoaded(it))
            }.launchIn(scope)

            getLecturersUseCase().onEach {
                dispatch(Action.LecturerNamesLoaded(it))
            }.launchIn(scope)

            getAudiencesUseCase().onEach {
                dispatch(Action.AudiencesLoaded(it))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.SaveLesson -> {
                    val state = getState()
                    val lessonName = state.lessonName

                    when(lessonName.isNotEmpty()){
                        true -> {
                            scope.launch {
                                updateWeekUseCase(
                                    weekId = state.weekId,
                                    dayName = state.dayName,
                                    lesson =  Lesson(
                                        id = state.futureLessonId,
                                        name = lessonName,
                                        start = state.start,
                                        end = state.end,
                                        lecturer = state.lecturer,
                                        audience = state.audience,
                                        typeOfLesson = state.typeOfLesson
                                    )
                                )
                                publish(Label.LessonSaved)
                            }
                        }

                        false -> {
                            publish(Label.AddLessonClickedAndLessonNameIsEmpty)
                        }
                    }
                }

                is Intent.ChangeLessonName -> {
                    dispatch(Msg.ChangeLessonName(intent.name))
                }

                is Intent.ChangeLecturer -> {
                    dispatch(Msg.ChangeLecturer(intent.lecturer))
                }

                is Intent.ChangeAudience -> {
                    dispatch(Msg.ChangeAudience(intent.audience))
                }

                is Intent.ChangeStart -> {
                    val state = getState()
                    if(intent.start >= state.end && state.end != 0L) {
                        publish(Label.StartTimeSaveClickedAndItsMoreThanEndTime)
                    } else {
                        dispatch(Msg.ChangeStart(intent.start))
                    }
                }

                is Intent.ChangeEnd -> {
                    val state = getState()
                    if(intent.end <= state.start) {
                        publish(Label.EndTimeSaveClickedAndItsLessThanStartTime)
                    } else {
                        dispatch(Msg.ChangeEnd(intent.end))
                    }
                }

                is Intent.ChangeTypeOfLesson -> {
                    dispatch(Msg.ChangeTypeOfLesson(intent.typeOfLesson))
                }

                Intent.NameLessonsListIsEmpty -> {
                    publish(Label.NameLessonsListIsEmpty)
                }

                Intent.LecturersListIsEmpty -> {
                    publish(Label.LecturersListIsEmpty)
                }

                Intent.AudiencesListIsEmpty -> {
                    publish(Label.AudiencesListIsEmpty)
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LessonNamesLoaded -> {
                    dispatch(Msg.LessonNamesLoaded(action.lessonNames))
                }

                is Action.LecturerNamesLoaded -> {
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

            is Msg.ChangeTypeOfLesson -> {
                copy(typeOfLesson = msg.typeOfLesson)
            }

            is Msg.ChangeEnd -> {
                copy(end = msg.end)
            }
            is Msg.ChangeStart -> {
                copy(start = msg.start)
            }
        }
    }
}