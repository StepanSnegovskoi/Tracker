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

class DefaultAddCategoryComponent @AssistedInject constructor(
    private val storeFactory: AddCategoryStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onCategorySaved") onCategorySaved: () -> Unit
) : AddCategoryComponent, ComponentContext by componentContext {

    val store = instanceKeeper.getStore { storeFactory.create() }

    init {
        store.labels.onEach {
            when (it) {
                AddCategoryStore.Label.AddCategoryClickedAndCategoryIsEmpty -> {
                    /** Nothing **/
                }

                AddCategoryStore.Label.CategorySaved -> {
                    onCategorySaved()
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

    override fun onAddCategoryClicked() {
        store.accept(AddCategoryStore.Intent.AddCategory)
    }

    override fun onClearCategoryClicked() {
        store.accept(AddCategoryStore.Intent.ChangeCategory(""))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onCategorySaved") onCategorySaved: () -> Unit
        ): DefaultAddCategoryComponent
    }
}