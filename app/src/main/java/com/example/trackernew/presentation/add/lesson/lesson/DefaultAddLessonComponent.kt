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
    @Assisted("ifLessonNamesAreEmpty") ifLessonNamesAreEmpty: () -> Unit,
    @Assisted("ifLecturersAreEmpty") ifLecturersAreEmpty: () -> Unit,
    @Assisted("ifAudiencesAreEmpty") ifAudiencesAreEmpty: () -> Unit,
    @Assisted("onLessonSaved") onLessonSaved: () -> Unit,
) : AddLessonComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }

    override val labels: Flow<AddLessonStore.Label>
        get() = store.labels

    init {
        store.labels.onEach {
            when (val label = it) {
                AddLessonStore.Label.LessonNamesClickedAndTheyAreEmpty -> {
                    ifLessonNamesAreEmpty()
                }

                AddLessonStore.Label.LecturersClickedAndTheyAreEmpty -> {
                    ifLecturersAreEmpty()
                }

                AddLessonStore.Label.AudiencesClickedAndTheyAreEmpty -> {
                    ifAudiencesAreEmpty()
                }

                AddLessonStore.Label.LessonSaved -> {
                    onLessonSaved()
                }
                /**
                 * Другие случаи обрабатываются не здесь
                 */
                else -> {
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddLessonStore.State> = store.stateFlow

    override fun onSaveLessonClicked() {
        store.accept(AddLessonStore.Intent.SaveLessonClicked)
    }

    override fun onNameChanged(name: String) {
        store.accept(AddLessonStore.Intent.ChangeLessonName(name))
    }

    override fun goToAddNameLessonContent() {
        store.accept(AddLessonStore.Intent.NameLessonsClickedAndTheyAreEmpty)
    }

    override fun goToAddLecturerContent() {
        store.accept(AddLessonStore.Intent.LecturersClickedAndTheyAreEmpty)
    }

    override fun onLecturerChanged(lecturer: String) {
        store.accept(AddLessonStore.Intent.ChangeLecturer(lecturer))
    }

    override fun goToAddAudienceContent() {
        store.accept(AddLessonStore.Intent.AudiencesClickedAndTheyAreEmpty)
    }

    override fun onAudienceChanged(audience: String) {
        store.accept(AddLessonStore.Intent.ChangeAudience(audience))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("ifLessonNamesAreEmpty") ifLessonNamesAreEmpty: () -> Unit,
            @Assisted("ifLecturersAreEmpty") ifLecturersAreEmpty: () -> Unit,
            @Assisted("ifAudiencesAreEmpty") ifAudiencesAreEmpty: () -> Unit,
            @Assisted("onLessonSaved") onLessonSaved: () -> Unit,
        ): DefaultAddLessonComponent
    }
}