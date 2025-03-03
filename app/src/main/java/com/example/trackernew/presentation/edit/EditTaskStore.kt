package com.example.trackernew.presentation.edit

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.usecase.EditTaskUseCase
import com.example.trackernew.presentation.edit.EditTaskStore.Intent
import com.example.trackernew.presentation.edit.EditTaskStore.Label
import com.example.trackernew.presentation.edit.EditTaskStore.State
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

interface EditTaskStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object EditTaskClicked : Intent

        data class ChangeName(val name: String) : Intent

        data class ChangeDescription(val description: String) : Intent

        data class ChangeCategory(val category: String) : Intent

        data class ChangeDeadline(val deadline: Long) : Intent
    }

    data class State(
        val id: Int,
        val name: String,
        val description: String,
        val category: String,
        val isCompleted: Boolean,
        val addingTime: Long,
        val deadline: Long,
    )

    sealed interface Label {
    }
}

class EditTaskStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editTaskUseCase: EditTaskUseCase
) {

    fun create(task: Task): EditTaskStore =
        object : EditTaskStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EditTaskStore",
            initialState = State(
                id = task.id,
                name = task.name,
                description = task.description,
                category = task.category,
                isCompleted = task.isCompleted,
                addingTime = task.addingTime,
                deadline = task.deadline
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
    }

    private sealed interface Msg {

        data class ChangeName(val name: String) : Msg

        data class ChangeDescription(val description: String) : Msg

        data class ChangeCategory(val category: String) : Msg

        data class ChangeDeadline(val deadline: Long) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when(intent){
                is Intent.ChangeCategory -> {
                    dispatch(Msg.ChangeCategory(intent.category))
                }
                is Intent.ChangeDeadline -> {
                    dispatch(Msg.ChangeDeadline(intent.deadline))
                }
                is Intent.ChangeDescription -> {
                    dispatch(Msg.ChangeDescription(intent.description))
                }
                is Intent.ChangeName -> {
                    dispatch(Msg.ChangeName(intent.name))
                }
                Intent.EditTaskClicked -> {
                    val state = getState()
                    scope.launch {
                        editTaskUseCase(
                            Task(
                                id = state.id,
                                name = state.name,
                                description = state.description,
                                category = state.category,
                                isCompleted = state.isCompleted,
                                addingTime = state.addingTime,
                                deadline = state.deadline
                            )
                        )
                    }
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
                is Msg.ChangeDeadline -> {
                    copy(deadline = msg.deadline)
                }
                is Msg.ChangeDescription -> {
                    copy(description = msg.description)
                }
                is Msg.ChangeName -> {
                    copy(name = msg.name)
                }
            }
    }
}
