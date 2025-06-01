package com.example.trackernew.presentation.tasks

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.domain.usecase.DeleteCategoryUseCase
import com.example.trackernew.domain.usecase.DeleteTaskByIdUseCase
import com.example.trackernew.domain.usecase.GetCategoriesUseCase
import com.example.trackernew.domain.usecase.GetTasksUseCase
import com.example.trackernew.presentation.extensions.filterBySortTypeAndCategory
import com.example.trackernew.presentation.tasks.TasksStore.Intent
import com.example.trackernew.presentation.tasks.TasksStore.Label
import com.example.trackernew.presentation.tasks.TasksStore.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface TasksStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickAddTask : Intent

        data object ClickAddCategory : Intent

        data class ClickDeleteCategory(val category: Category) : Intent

        data object ClickSchedule : Intent

        data class ClickDeleteTask(val task: Task) : Intent

        data class LongClickTask(val task: Task) : Intent


        data class ChangeSort(val sort: Sort) : Intent

        data class ChangeCategory(val category: Category) : Intent
    }

    data class State(
        val tasksState: TasksState, // Заменяем tasks на tasksState
        val categories: List<Category>,
        val sort: Sort,
        val category: Category
    )

    sealed class TasksState {
        data object Loading : TasksState()
        data class Loaded(val tasks: Tasks) : TasksState()
    }

    data class Tasks(
        val currentTasks: List<Task>,
        val filteredTasks: List<Task>
    )

    sealed interface Label {

        data object ClickAddTask : Label

        data object ClickAddCategory : Label

        data object ClickSchedule : Label

        data class LongClickTask(val task: Task) : Label

    }
}

class TasksStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getTasksUseCase: GetTasksUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) {

    fun create(): TasksStore =
        object : TasksStore, Store<Intent, State, Label> by storeFactory.create(
            name = "TasksStore",
            initialState = State(
                tasksState = TasksStore.TasksState.Loading,
                sort = Sort.ByName,
                category = Category("Всё вместе"),
                categories = emptyList()
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class TasksLoaded(val tasks: List<Task>) : Action

        data class CategoriesLoaded(val categories: List<Category>) : Action
    }

    private sealed interface Msg {

        data class TasksLoaded(val tasks: List<Task>) : Msg

        data class CategoriesLoaded(val categories: List<Category>) : Msg


        data class ChangeSort(val sort: Sort) : Msg

        data class ChangeCategory(val category: Category) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            getTasksUseCase().onEach {
                dispatch(Action.TasksLoaded(it))
            }.launchIn(scope)

            getCategoriesUseCase().onEach {
                dispatch(Action.CategoriesLoaded(it))
            }.launchIn(scope)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAddTask -> {
                    publish(Label.ClickAddTask)
                }

                is Intent.LongClickTask -> {
                    publish(Label.LongClickTask(intent.task))
                }

                is Intent.ChangeSort -> {
                    dispatch(Msg.ChangeSort(intent.sort))
                }

                is Intent.ChangeCategory -> {
                    dispatch(Msg.ChangeCategory(intent.category))
                }

                is Intent.ClickDeleteTask -> {
                    val id = intent.task.id
                    scope.launch {
                        deleteTaskByIdUseCase(id)
                    }
                }

                Intent.ClickAddCategory -> {
                    publish(Label.ClickAddCategory)
                }

                Intent.ClickSchedule -> {
                    publish(Label.ClickSchedule)
                }

                is Intent.ClickDeleteCategory -> {
                    val state = getState()
                    scope.launch {
                        deleteCategoryUseCase(intent.category)
                    }

                    if (state.category == intent.category) {
                        dispatch(Msg.ChangeCategory(Category("Всё вместе")))
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.TasksLoaded -> {
                    dispatch(Msg.TasksLoaded(action.tasks))
                }

                is Action.CategoriesLoaded -> {
                    dispatch(Msg.CategoriesLoaded(action.categories))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.TasksLoaded -> {
                    copy(
                        tasksState = TasksStore.TasksState.Loaded( // Устанавливаем Loaded состояние
                            TasksStore.Tasks(
                                currentTasks = msg.tasks,
                                filteredTasks = msg.tasks
                                    .filterBySortTypeAndCategory(sort, category)
                            )
                        )
                    )
                }

                is Msg.ChangeSort -> {
                    when (tasksState) {
                        is TasksStore.TasksState.Loaded -> {
                            copy(
                                sort = msg.sort,
                                tasksState = TasksStore.TasksState.Loaded(
                                    TasksStore.Tasks(
                                        currentTasks = tasksState.tasks.currentTasks,
                                        filteredTasks = tasksState.tasks.currentTasks
                                            .filterBySortTypeAndCategory(msg.sort, category)
                                    )
                                )
                            )
                        }
                        TasksStore.TasksState.Loading -> this
                    }
                }

                is Msg.ChangeCategory -> {
                    when (tasksState) {
                        is TasksStore.TasksState.Loaded -> {
                            copy(
                                category = msg.category,
                                tasksState = TasksStore.TasksState.Loaded(
                                    TasksStore.Tasks(
                                        currentTasks = tasksState.tasks.currentTasks,
                                        filteredTasks = tasksState.tasks.currentTasks
                                            .filterBySortTypeAndCategory(sort, msg.category)
                                    )
                                )
                            )
                        }
                        TasksStore.TasksState.Loading -> this
                    }
                }

                is Msg.CategoriesLoaded -> {
                    copy(categories = msg.categories)
                }
            }
    }
}
