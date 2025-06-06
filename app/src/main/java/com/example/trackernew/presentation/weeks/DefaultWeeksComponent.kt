package com.example.trackernew.presentation.weeks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.domain.entity.Week
import com.example.trackernew.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultWeeksComponent @AssistedInject constructor (
    private val storeFactory: WeeksStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onConfirmEditButtonClicked") onConfirmEditButtonClicked: () -> Unit
): WeeksComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it) {
                WeeksStore.Label.ClickConfirmEditButton -> {
                    onConfirmEditButtonClicked()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<WeeksStore.State> = store.stateFlow

    override fun onWeekStatusChanged(week: Week) {
        store.accept(WeeksStore.Intent.ChangeWeekStatus(week))
    }

    override fun onConfirmEditClicked() {
        store.accept(WeeksStore.Intent.ConfirmEdit)
    }

    override fun onSelectWeekAsCurrentClicked(week: Week) {
        store.accept(WeeksStore.Intent.SelectWeekAsCurrent(week))
    }

    override fun onDeleteWeekClicked(weekId: Int) {
        store.accept(WeeksStore.Intent.DeleteWeek(weekId))
    }

    override fun onMoveUpWeekClicked(week: Week) {
        store.accept(WeeksStore.Intent.MoveUpWeek(week))
    }

    override fun onMoveDownWeekClicked(week: Week) {
        store.accept(WeeksStore.Intent.MoveDownWeek(week))
    }

    @AssistedFactory
    interface Factory {
        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onConfirmEditButtonClicked") onConfirmEditButtonClicked: () -> Unit,
        ): DefaultWeeksComponent
    }
}