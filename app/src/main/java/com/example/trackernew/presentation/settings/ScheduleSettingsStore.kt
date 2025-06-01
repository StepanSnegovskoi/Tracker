package com.example.trackernew.presentation.settings

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.entity.Lecturer
import com.example.trackernew.domain.entity.LessonName
import com.example.trackernew.domain.usecase.DeleteAudienceUseCase
import com.example.trackernew.domain.usecase.DeleteLecturerUseCase
import com.example.trackernew.domain.usecase.DeleteLessonNameUseCase
import com.example.trackernew.domain.usecase.GetAudiencesUseCase
import com.example.trackernew.domain.usecase.GetLecturersUseCase
import com.example.trackernew.domain.usecase.GetLessonNamesUseCase
import com.example.trackernew.presentation.settings.ScheduleSettingsStore.Intent
import com.example.trackernew.presentation.settings.ScheduleSettingsStore.State
import com.example.trackernew.presentation.settings.ScheduleSettingsStore.Label
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ScheduleSettingsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data class ClickDeleteLecturer(val name: String) : Intent

        data class ClickDeleteLessonName(val name: String) : Intent

        data class ClickDeleteAudience(val name: String) : Intent
    }

    data class State(
        val lecturers: List<Lecturer>,
        val lessonNames: List<LessonName>,
        val audiences: List<Audience>
    )

    sealed interface Label {
    }
}

class ScheduleSettingsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getLecturersUseCase: GetLecturersUseCase,
    private val getLessonNamesUseCase: GetLessonNamesUseCase,
    private val getAudiencesUseCase: GetAudiencesUseCase,
    private val deleteLecturerUseCase: DeleteLecturerUseCase,
    private val deleteLessonNameUseCase: DeleteLessonNameUseCase,
    private val deleteAudienceUseCase: DeleteAudienceUseCase
) {

    fun create(): ScheduleSettingsStore =
        object : ScheduleSettingsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ScheduleSettingsStore",
            initialState = State(
                lecturers = emptyList(),
                lessonNames = emptyList(),
                audiences = emptyList()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class LecturersLoaded(val lecturers: List<Lecturer>) : Action

        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Action

        data class AudiencesLoaded(val audiences: List<Audience>) : Action
    }

    private sealed interface Msg {

        data class LecturersLoaded(val lecturers: List<Lecturer>) : Msg

        data class LessonNamesLoaded(val lessonNames: List<LessonName>) : Msg

        data class AudiencesLoaded(val audiences: List<Audience>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getLecturersUseCase().onEach {
                dispatch(Action.LecturersLoaded(it))
            }.launchIn(scope)

            getLessonNamesUseCase().onEach {
                dispatch(Action.LessonNamesLoaded(it))
            }.launchIn(scope)

            getAudiencesUseCase().onEach {
                dispatch(Action.AudiencesLoaded(it))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent) {
                is Intent.ClickDeleteAudience -> {
                    scope.launch {
                        deleteAudienceUseCase(intent.name)
                    }
                }

                is Intent.ClickDeleteLecturer -> {
                    scope.launch {
                        deleteLecturerUseCase(intent.name)
                    }
                }

                is Intent.ClickDeleteLessonName -> {
                    scope.launch {
                        deleteLessonNameUseCase(intent.name)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when(action){
                is Action.AudiencesLoaded -> {
                    dispatch(Msg.AudiencesLoaded(action.audiences))
                }
                is Action.LecturersLoaded -> {
                    dispatch(Msg.LecturersLoaded(action.lecturers))
                }
                is Action.LessonNamesLoaded -> {
                    dispatch(Msg.LessonNamesLoaded(action.lessonNames))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when(msg) {
            is Msg.AudiencesLoaded -> {
                copy(audiences = msg.audiences)
            }
            is Msg.LecturersLoaded -> {
                copy(lecturers = msg.lecturers)
            }
            is Msg.LessonNamesLoaded -> {
                copy(lessonNames = msg.lessonNames)
            }
        }
    }
}
