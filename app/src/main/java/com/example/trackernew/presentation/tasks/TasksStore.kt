package com.example.trackernew.presentation.tasks

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.usecase.GetTasksUseCase
import com.example.trackernew.presentation.tasks.TasksStore.Intent
import com.example.trackernew.presentation.tasks.TasksStore.Label
import com.example.trackernew.presentation.tasks.TasksStore.State
import com.example.trackernew.presentation.utils.Sort
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface TasksStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickAdd : Intent

        data class LongClickTask(val task: Task) : Intent

        data class ChangeSort(val sort: Sort) : Intent
    }

    data class State(
        val tasks: List<Task>,
        val sort: Sort
    )

    sealed interface Label {

        data object ClickAdd : Label

        data class LongClickTask(val task: Task) : Label
    }
}

class TasksStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getTasksUseCase: GetTasksUseCase
) {

    fun create(): TasksStore =
        object : TasksStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TasksStore",
            initialState = State(
                tasks = listOf(),
                sort = Sort.ByName
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class TasksLoaded(val tasks: List<Task>) : Action
    }

    private sealed interface Msg {

        data class TasksLoaded(val tasks: List<Task>) : Msg

        data class ChangeSort(val sort: Sort) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getTasksUseCase()
                .onEach {
                    dispatch(Action.TasksLoaded(it))
                }.launchIn(scope)
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAdd -> {
                    publish(Label.ClickAdd)
                }

                is Intent.LongClickTask -> {
                    publish(Label.LongClickTask(intent.task))
                }

                is Intent.ChangeSort -> {
                    dispatch(Msg.ChangeSort(intent.sort))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.TasksLoaded -> {
                    dispatch(Msg.TasksLoaded(action.tasks))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.TasksLoaded -> {
                    copy(tasks = msg.tasks)
                }

                is Msg.ChangeSort -> {
                    copy(
                        sort = msg.sort,
                        tasks = tasks.sortedWith(msg.sort.comparator())
                    )
                }
            }
    }
}
