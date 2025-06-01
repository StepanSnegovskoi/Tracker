package com.example.trackernew.presentation.add.lesson.audience

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.trackernew.presentation.add.category.AddCategoryStore
import com.example.trackernew.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DefaultAddAudienceComponent @AssistedInject constructor (
    private val storeFactory: AddAudienceStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onAudienceSaved") onAudienceSaved: () -> Unit
): AddAudienceComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(it){
                AddAudienceStore.Label.AddAudienceClickedAndAudienceIsEmpty -> {
                    /** Nothing **/
                }

                AddAudienceStore.Label.AudienceSaved -> {
                    onAudienceSaved()
                }
            }
        }.launchIn(componentScope())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddAudienceStore.State> = store.stateFlow

    override val labels: Flow<AddAudienceStore.Label> = store.labels

    override fun onAddAudienceClicked() {
        store.accept(AddAudienceStore.Intent.AddAudience)
    }

    override fun onAudienceChanged(audience: String) {
        store.accept(AddAudienceStore.Intent.ChangeAudience(audience))
    }

    override fun onClearAudienceClicked() {
        store.accept(AddAudienceStore.Intent.ChangeAudience(""))
    }

    @AssistedFactory
    interface Factory {
        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onAudienceSaved") onAudienceSaved: () -> Unit
        ): DefaultAddAudienceComponent
    }
}