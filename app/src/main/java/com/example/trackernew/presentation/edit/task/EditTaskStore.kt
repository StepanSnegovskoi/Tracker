package com.example.trackernew.presentation.edit.task

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.trackernew.domain.entity.Category
import com.example.trackernew.domain.entity.SubTask
import com.example.trackernew.domain.entity.Task
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.domain.usecase.DeleteAlarmUseCase
import com.example.trackernew.domain.usecase.EditTaskUseCase
import com.example.trackernew.domain.usecase.GetCategoriesUseCase
import com.example.trackernew.domain.usecase.SetAlarmUseCase
import com.example.trackernew.presentation.edit.task.EditTaskStore.Intent
import com.example.trackernew.presentation.edit.task.EditTaskStore.Label
import com.example.trackernew.presentation.edit.task.EditTaskStore.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val ONE = 1
private const val ONE_HUNDRED = 100
private const val SECOND_STRING = "Секунда"
private const val MINUTE_STRING = "Минута"
private const val HOUR_STRING = "Час"
private const val DAY_STRING = "День"
private const val UNKNOWN_TIME = "Неизвестное время"

val listOfTimeUnits = listOf(SECOND_STRING, MINUTE_STRING, HOUR_STRING, DAY_STRING)

val listFromOneToOneHundred = (ONE..ONE_HUNDRED).toList().map { it.toString() }

private const val MILLIS_IN_SECOND = 1_000
private const val MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60
private const val MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60
private const val MILLIS_IN_DAY = MILLIS_IN_HOUR * 24

interface EditTaskStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object EditTask : Intent

        data object AddSubTask : Intent

        data class DeleteSubTask(val id: Int) : Intent


        data class ChangeName(val name: String) : Intent

        data class ChangeDescription(val description: String) : Intent

        data class ChangeCategory(val category: String) : Intent

        data class ChangeDeadline(val deadline: Long) : Intent

        data class ChangeSubTask(val subTask: String) : Intent

        data class ChangeSubTaskStatus(val id: Int) : Intent

        data class ChangeTimesCount(val timesCount: Int) : Intent

        data class ChangeTimeForDeadline(val timeForDeadline: String) : Intent

        data object ChangeTaskStatus : Intent

        data object ChangeAlarmEnable : Intent
    }

    data class State(
        val id: Int,
        val name: String,
        val description: String,
        val category: String,
        val status: TaskStatus,
        val deadline: Long,
        val subTasks: List<SubTask>,
        val categories: List<Category>,
        val addingTime: Long,
        val subTask: String,
        val alarmEnable: Boolean,
        val timeUnitCount: Int,
        val timeUnit: String
    )

    sealed interface Label {

        data object TaskEdited : Label

        data object SubTaskSaved : Label


        data object EditTaskClickedAndNameIsEmpty : Label

        data object AddSubTaskClickedAndNameIsEmpty : Label

        data object EditDeadlineClickedAndDeadlineIsIncorrect : Label

        data object EditDeadlineClickedAndReminderIsIncorrect : Label
    }
}

class EditTaskStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val editTaskUseCase: EditTaskUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val setAlarmUseCase: SetAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) {

    fun create(task: Task): EditTaskStore =
        object : EditTaskStore, Store<Intent, State, Label> by storeFactory.create(
            name = "EditTaskStore",
            initialState = State(
                id = task.id,
                name = task.name,
                description = task.description,
                category = task.category,
                status = task.status,
                deadline = task.deadline,
                subTasks = task.subTasks,
                categories = listOf(),
                addingTime = task.addingTime,
                subTask = "",
                alarmEnable = task.alarmEnable,
                timeUnitCount = task.timeUnitCount,
                timeUnit = task.timeUnit
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

        data object ChangeTaskStatus : Msg

        data class ChangeSubTaskStatus(val id: Int) : Msg

        data object ChangeAlarmEnable : Msg

        data class ChangeTimesCount(val timesCount: Int) : Msg

        data class ChangeTimeForDeadline(val timeForDeadline: String) : Msg


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
                    when (intent.deadline < System.currentTimeMillis() && intent.deadline != 0L) {
                        true -> {
                            publish(Label.EditDeadlineClickedAndDeadlineIsIncorrect)
                        }

                        false -> {
                            dispatch(Msg.ChangeDeadline(intent.deadline))
                        }
                    }
                }

                is Intent.ChangeDescription -> {
                    dispatch(Msg.ChangeDescription(intent.description))
                }

                is Intent.ChangeName -> {
                    dispatch(Msg.ChangeName(intent.name))
                }

                Intent.EditTask -> {
                    val state = getState()
                    val name = state.name.trim()

                    when (name.isNotEmpty()) {
                        true -> {
                            scope.launch {
                                val task = Task(
                                    id = state.id,
                                    name = state.name.trim(),
                                    description = state.description.trim(),
                                    category = state.category,
                                    status = state.status,
                                    addingTime = state.addingTime,
                                    deadline = state.deadline,
                                    subTasks = state.subTasks,
                                    alarmEnable = state.alarmEnable,
                                    timeUnit = state.timeUnit,
                                    timeUnitCount = state.timeUnitCount
                                )

                                val timeUnit = when (state.timeUnit) {
                                    SECOND_STRING -> {
                                        MILLIS_IN_SECOND
                                    }

                                    MINUTE_STRING -> {
                                        MILLIS_IN_MINUTE
                                    }

                                    HOUR_STRING -> {
                                        MILLIS_IN_HOUR
                                    }

                                    DAY_STRING -> {
                                        MILLIS_IN_DAY
                                    }

                                    else -> {
                                        error(UNKNOWN_TIME)
                                    }
                                }

                                val timeForDeadline = task.deadline - state.timeUnitCount * timeUnit

                                when (task.alarmEnable) {
                                    true -> {
                                        if (timeForDeadline < Calendar.getInstance().timeInMillis) {
                                            publish(Label.EditDeadlineClickedAndReminderIsIncorrect)
                                            return@launch
                                        } else {
                                            setAlarmUseCase(task, timeForDeadline, task.id)
                                        }
                                    }

                                    false -> {
                                        deleteAlarmUseCase(task.id)
                                    }
                                }

                                editTaskUseCase(
                                    task
                                )

                                publish(Label.TaskEdited)
                            }
                        }

                        false -> {
                            publish(Label.EditTaskClickedAndNameIsEmpty)
                        }
                    }
                }

                Intent.ChangeTaskStatus -> {
                    dispatch(Msg.ChangeTaskStatus)
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

                is Intent.ChangeSubTaskStatus -> {
                    dispatch(Msg.ChangeSubTaskStatus(intent.id))
                }

                Intent.ChangeAlarmEnable -> {
                    dispatch(Msg.ChangeAlarmEnable)
                }

                is Intent.ChangeTimeForDeadline -> {
                    dispatch(Msg.ChangeTimeForDeadline(intent.timeForDeadline))
                }

                is Intent.ChangeTimesCount -> {
                    dispatch(Msg.ChangeTimesCount(intent.timesCount))
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

                is Msg.CategoriesLoaded -> {
                    copy(categories = msg.categories)
                }

                Msg.ChangeTaskStatus -> {
                    val status = when (status) {
                        TaskStatus.Completed -> {
                            TaskStatus.InTheProcess
                        }

                        TaskStatus.Executed -> {
                            TaskStatus.Failed
                        }

                        TaskStatus.Failed -> {
                            TaskStatus.Completed
                        }

                        TaskStatus.InTheProcess -> {
                            TaskStatus.Executed
                        }
                    }
                    copy(status = status)
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

                is Msg.ChangeSubTaskStatus -> {
                    copy(subTasks = buildList {
                        subTasks.forEach { subTask ->
                            when (subTask.id == msg.id) {
                                true -> {
                                    add(subTask.copy(isCompleted = !subTask.isCompleted))
                                }

                                false -> {
                                    add(subTask)
                                }
                            }
                        }
                    })
                }

                Msg.ChangeAlarmEnable -> {
                    copy(alarmEnable = !alarmEnable)
                }

                is Msg.ChangeTimeForDeadline -> {
                    copy(timeUnit = msg.timeForDeadline)
                }

                is Msg.ChangeTimesCount -> {
                    copy(timeUnitCount = msg.timesCount)
                }
            }
    }
}

