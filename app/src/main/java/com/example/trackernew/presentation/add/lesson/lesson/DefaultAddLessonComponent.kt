package com.example.trackernew.presentation.add.lesson.lesson

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultAddLessonComponent @AssistedInject constructor(
    private val storeFactory: AddLessonFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("weekId") weekId: Int,
    @Assisted("dayName") dayName: String,
    @Assisted("futureLessonId") futureLessonId: Int,
    @Assisted("onLessonNamesListIsEmpty") onLessonNamesListIsEmpty: () -> Unit,
    @Assisted("onLecturersListIsEmpty") onLecturersListIsEmpty: () -> Unit,
    @Assisted("onAudiencesListIsEmpty") onAudiencesListIsEmpty: () -> Unit,
    @Assisted("onLessonSaved") onLessonSaved: () -> Unit,
) : AddLessonComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create(weekId, dayName, futureLessonId)
    }

    override val labels: Flow<AddLessonStore.Label>
        get() = store.labels

    init {
        store.labels.onEach {
            when (it) {
                AddLessonStore.Label.AddLessonClickedAndLessonNameIsEmpty -> {
                    /** Nothing **/
                }

                AddLessonStore.Label.AddLessonClickedAndTimeIsIncorrect -> {
                    /** Nothing **/
                }

                AddLessonStore.Label.NameLessonsListIsEmpty -> {
                    onLessonNamesListIsEmpty()
                }

                AddLessonStore.Label.LecturersListIsEmpty -> {
                    onLecturersListIsEmpty()
                }

                AddLessonStore.Label.AudiencesListIsEmpty -> {
                    onAudiencesListIsEmpty()
                }

                AddLessonStore.Label.LessonSaved -> {
                    onLessonSaved()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddLessonStore.State> = store.stateFlow

    override fun onAddLessonClicked() {
        store.accept(AddLessonStore.Intent.AddLesson)
    }

    override fun onLessonNameChanged(name: String) {
        store.accept(AddLessonStore.Intent.ChangeLessonName(name))
    }

    override fun onLecturerChanged(lecturer: String) {
        store.accept(AddLessonStore.Intent.ChangeLecturer(lecturer))
    }

    override fun onAudienceChanged(audience: String) {
        store.accept(AddLessonStore.Intent.ChangeAudience(audience))
    }

    override fun onStartChanged(start: Long) {
        store.accept(AddLessonStore.Intent.ChangeStart(start))
    }

    override fun onEndChanged(end: Long) {
        store.accept(AddLessonStore.Intent.ChangeEnd(end))
    }

    override fun onTypeOfLessonChanged(typeOfLesson: String) {
        store.accept(AddLessonStore.Intent.ChangeTypeOfLesson(typeOfLesson))
    }

    override fun onLessonNameClickedAndLessonNamesListIsEmpty() {
        store.accept(AddLessonStore.Intent.NameLessonsListIsEmpty)
    }

    override fun onLecturerClickedAndLecturersListIsEmpty() {
        store.accept(AddLessonStore.Intent.LecturersListIsEmpty)
    }

    override fun onAudienceClickedAndAudiencesListIsEmpty() {
        store.accept(AddLessonStore.Intent.AudiencesListIsEmpty)
    }

    override fun onClearLessonNameClicked() {
        store.accept(AddLessonStore.Intent.ChangeLessonName(""))
    }

    override fun onClearLecturerClicked() {
        store.accept(AddLessonStore.Intent.ChangeLecturer(""))
    }

    override fun onClearAudienceClicked() {
        store.accept(AddLessonStore.Intent.ChangeAudience(""))
    }

    override fun onClearStartClicked() {
        store.accept(AddLessonStore.Intent.ChangeStart(0L))
    }

    override fun onClearEndClicked() {
        store.accept(AddLessonStore.Intent.ChangeEnd(0L))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("weekId") weekId: Int,
            @Assisted("dayName") dayName: String,
            @Assisted("futureLessonId") futureLessonId: Int,
            @Assisted("onLessonNamesListIsEmpty") onLessonNamesListIsEmpty: () -> Unit,
            @Assisted("onLecturersListIsEmpty") onLecturersListIsEmpty: () -> Unit,
            @Assisted("onAudiencesListIsEmpty") onAudiencesListIsEmpty: () -> Unit,
            @Assisted("onLessonSaved") onLessonSaved: () -> Unit,
        ): DefaultAddLessonComponent
    }
}