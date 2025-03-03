package com.example.trackernew.presentation.add.category

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultAddCategoryComponent @AssistedInject constructor (
    @Assisted("componentContext") componentContext: ComponentContext,
    private val storeFactory: AddCategoryStoreFactory
): AddCategoryComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<AddCategoryStore.State> = store.stateFlow

    override fun onCategoryChanged(category: String) {
        store.accept(AddCategoryStore.Intent.ChangeCategory(category))
    }

    override fun onAddClicked() {
        store.accept(AddCategoryStore.Intent.AddCategoryClicked)
    }

    @AssistedFactory
    interface Factory {

        fun create (
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultAddCategoryComponent
    }
}