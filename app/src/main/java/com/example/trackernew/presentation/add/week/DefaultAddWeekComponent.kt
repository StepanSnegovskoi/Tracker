package com.example.trackernew.presentation.add.week

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

class DefaultAddWeekComponent @AssistedInject constructor (
    private val storeFactory: AddWeekStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onWeekSaved") onWeekSaved: () -> Unit
): AddWeekComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                AddWeekStore.Label.AddWeekClickedAndWeekIsEmpty -> {
                    /** Nothing **/
                }

                AddWeekStore.Label.WeekSaved -> {
                    onWeekSaved()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddWeekStore.State> = store.stateFlow

    override val labels: Flow<AddWeekStore.Label> = store.labels

    override fun onWeekChanged(week: String) {
        store.accept(AddWeekStore.Intent.ChangeWeek(week))
    }

    override fun onAddWeekClicked() {
        store.accept(AddWeekStore.Intent.AddWeek)
    }

    @AssistedFactory
    interface Factory {
        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onWeekSaved") onWeekSaved: () -> Unit
        ): DefaultAddWeekComponent
    }
}