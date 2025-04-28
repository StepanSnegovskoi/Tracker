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
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onWeekSaved") onWeekSaved: () -> Unit,
    private val storeFactory: AddWeekStoreFactory
): AddWeekComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(val label = it){
                AddWeekStore.Label.WeekSaved -> {
                    onWeekSaved()
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
    override val model: StateFlow<AddWeekStore.State> = store.stateFlow

    override val labels: Flow<AddWeekStore.Label> = store.labels

    override fun onWeekChanged(week: String) {
        store.accept(AddWeekStore.Intent.ChangeWeek(week))
    }

    override fun onAddClicked() {
        store.accept(AddWeekStore.Intent.AddWeekClicked)
    }

    @AssistedFactory
    interface Factory {

        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onWeekSaved") onWeekSaved: () -> Unit
        ): DefaultAddWeekComponent
    }
}