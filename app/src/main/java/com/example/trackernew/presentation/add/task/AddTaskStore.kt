package com.example.trackernew.presentation.add.task

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.SubTask
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.domain.usecase.GetCategoriesUseCase
import com.example.trackernew.domain.usecase.SaveTaskUseCase
import com.example.trackernew.presentation.add.task.AddTaskStore.Intent
import com.example.trackernew.presentation.add.task.AddTaskStore.Label
import com.example.trackernew.presentation.add.task.AddTaskStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

interface AddTaskStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object AddTask : Intent

        data object AddSubTask : Intent

        data class DeleteSubTask(val id: Int) : Intent


        data class ChangeName(val name: String) : Intent

        data class ChangeDescription(val description: String) : Intent

        data class ChangeCategory(val category: String) : Intent

        data class ChangeDeadline(val deadline: Long) : Intent

        data class ChangeSubTask(val subTask: String) : Intent

        data object CategoriesClickedAndTheyAreEmpty : Intent
    }

    data class State(
        val name: String,
        val description: String,
        val category: String,
        val deadline: Long,
        val subTasks: List<SubTask>,
        val categories: List<Category>,
        val subTask: String
    )

    sealed interface Label {

        data object TaskSaved : Label

        data object SubTaskSaved : Label


        data object CategoriesListIsEmpty : Label


        data object AddTaskClickedAndNameIsEmpty : Label

        data object AddSubTaskClickedAndNameIsEmpty : Label
    }
}

class AddTaskStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) {

    fun create(): AddTaskStore =
        object : AddTaskStore, Store<Intent, State, Label> by storeFactory.create(
            name = "AddTaskStore",
            initialState = State(
                name = "",
                description = "",
                category = "",
                deadline = 0,
                subTasks = listOf(),
                categories = listOf(),
                subTask = ""
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class CategoriesLoaded(val categories: List<Category>) : Action
    }

    private sealed interface Msg {

        data object AddSubTask : Msg

        data class DeleteSubTask(val id: Int) : Msg


        data class ChangeName(val name: String) : Msg

        data class ChangeDescription(val description: String) : Msg

        data class ChangeCategory(val category: String) : Msg

        data class ChangeDeadline(val deadline: Long) : Msg

        data class ChangeSubTask(val subTask: String) : Msg


        data class CategoriesLoaded(val categories: List<Category>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getCategoriesUseCase().onEach {
                dispatch(Action.CategoriesLoaded(it))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
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

                Intent.AddTask -> {
                    val state = getState()
                    if (state.name.trim().isEmpty()) {
                        publish(Label.AddTaskClickedAndNameIsEmpty)
                        return
                    }
                    scope.launch {
                        saveTaskUseCase(
                            Task(
                                id = 0,
                                name = state.name.trim(),
                                description = state.description.trim(),
                                category = state.category,
                                status = TaskStatus.InTheProcess,
                                addingTime = Calendar.getInstance().timeInMillis,
                                deadline = state.deadline,
                                subTasks = state.subTasks
                            )
                        )
                        publish(Label.TaskSaved)
                    }
                }

                Intent.CategoriesClickedAndTheyAreEmpty -> {
                    publish(Label.CategoriesListIsEmpty)
                }

                is Intent.ChangeSubTask -> {
                    dispatch(Msg.ChangeSubTask(intent.subTask))
                }

                Intent.AddSubTask -> {
                    val state = getState()
                    when (state.subTask.trim().isNotEmpty()) {
                        true -> {
                            dispatch(Msg.AddSubTask)
                            publish(Label.SubTaskSaved)
                        }

                        false -> {
                            publish(Label.AddSubTaskClickedAndNameIsEmpty)
                        }
                    }
                }

                is Intent.DeleteSubTask -> {
                    dispatch(Msg.DeleteSubTask(intent.id))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.CategoriesLoaded -> {
                    dispatch(Msg.CategoriesLoaded(action.categories))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
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

            is Msg.CategoriesLoaded -> {
                copy(categories = msg.categories)
            }

            is Msg.ChangeSubTask -> {
                copy(subTask = msg.subTask)
            }

            is Msg.AddSubTask -> {
                copy(
                    subTasks = buildList {
                        subTasks.forEach { add(it) }

                        val id = subTasks.maxOfOrNull { it.id }?.plus(1) ?: 0

                        add(
                            SubTask(
                                id = id,
                                name = subTask.trim(),
                                isCompleted = false
                            )
                        )
                    }
                )
            }

            is Msg.DeleteSubTask -> {
                copy(subTasks = buildList {
                    subTasks.forEach {
                        if (it.id != msg.id) {
                            add(it)
                        }
                    }
                })
            }
        }
    }
}
