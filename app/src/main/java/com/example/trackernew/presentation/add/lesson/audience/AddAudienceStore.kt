package com.example.trackernew.presentation.add.lesson.audience

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Audience
import com.example.trackernew.domain.usecase.AddAudienceUseCase
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceStore.Intent
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceStore.Label
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AddAudienceStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddAudience : Intent

        data class ChangeAudience(val audience: String) : Intent
    }

    data class State(
        val audience: String
    )

    sealed interface Label {

        data object AudienceSaved : Label

        data object AddAudienceClickedAndAudienceIsEmpty : Label
    }
}

class AddAudienceStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val addAudienceUseCase: AddAudienceUseCase
) {

    fun create(): AddAudienceStore =
        object : AddAudienceStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddAudienceStore",
            initialState = State(
                audience = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeAudience(val audience: String) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.AddAudience -> {
                    val state = getState()
                    val audience = state.audience.trim()

                    when (audience.isNotEmpty()) {
                        true -> {
                            scope.launch {
                                addAudienceUseCase(
                                    Audience(
                                        name = audience
                                    )
                                )
                                publish(Label.AudienceSaved)
                            }
                        }

                        false -> {
                            publish(Label.AddAudienceClickedAndAudienceIsEmpty)
                        }
                    }
                }

                is Intent.ChangeAudience -> {
                    dispatch(Msg.ChangeAudience(intent.audience))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ChangeAudience -> {
                    copy(audience = msg.audience)
                }
            }
    }
}
