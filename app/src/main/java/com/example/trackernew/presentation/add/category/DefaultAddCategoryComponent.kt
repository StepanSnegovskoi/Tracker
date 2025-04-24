package com.example.trackernew.presentation.add.category

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

class DefaultAddCategoryComponent @AssistedInject constructor (
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCategorySaved") onCategorySaved: () -> Unit,
    private val storeFactory: AddCategoryStoreFactory
): AddCategoryComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when(val label = it){
                AddCategoryStore.Label.CategorySaved -> {
                    onCategorySaved()
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
    override val model: StateFlow<AddCategoryStore.State> = store.stateFlow

    override val labels: Flow<AddCategoryStore.Label> = store.labels

    override fun onCategoryChanged(category: String) {
        store.accept(AddCategoryStore.Intent.ChangeCategory(category))
    }

    override fun onAddClicked() {
        store.accept(AddCategoryStore.Intent.AddCategoryClicked)
    }

    @AssistedFactory
    interface Factory {

        fun create (
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCategorySaved") onCategorySaved: () -> Unit
        ): DefaultAddCategoryComponent
    }
}