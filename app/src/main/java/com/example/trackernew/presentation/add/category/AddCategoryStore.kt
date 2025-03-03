package com.example.trackernew.presentation.add.category

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.usecase.AddCategoryUseCase
import com.example.trackernew.presentation.add.category.AddCategoryStore.Intent
import com.example.trackernew.presentation.add.category.AddCategoryStore.Label
import com.example.trackernew.presentation.add.category.AddCategoryStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddCategoryStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddCategoryClicked : Intent

        data class ChangeCategory(val category: String) : Intent
    }

    data class State(
        val category: String
    )

    sealed interface Label {
    }
}

class AddCategoryStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addCategoryUseCase: AddCategoryUseCase
) {

    fun create(): AddCategoryStore =
        object : AddCategoryStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddCategoryStore",
            initialState = State(
                ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeCategory(val category: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddCategoryClicked -> {
                    val state = getState()
                    scope.launch {
                        addCategoryUseCase(
                            Category(
                                name = state.category
                            )
                        )
                    }
                }

                is Intent.ChangeCategory -> {
                    dispatch(Msg.ChangeCategory(intent.category))
                }
            }

        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeCategory -> {
                    copy(category = msg.category)
                }
            }
    }
}
