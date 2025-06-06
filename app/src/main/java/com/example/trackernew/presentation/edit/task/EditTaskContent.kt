package com.example.trackernew.presentation.edit.task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.domain.entity.TaskStatus
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Black300
import com.example.trackernew.ui.theme.Green200
import com.example.trackernew.ui.theme.Orange100
import com.example.trackernew.ui.theme.Red300
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getDatePickerColors
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun EditTaskContent(component: EditTaskComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                EditTaskStore.Label.TaskEdited -> {
                    snackBarManager.showMessage(context.getString(R.string.task_edited))
                }

                EditTaskStore.Label.SubTaskSaved -> {
                    snackBarManager.showMessage(context.getString(R.string.subtask_saved))
                }

                EditTaskStore.Label.EditTaskClickedAndNameIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }

                EditTaskStore.Label.AddSubTaskClickedAndNameIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }

                EditTaskStore.Label.EditDeadlineClickedAndDeadlineIsIncorrect -> {
                    snackBarManager.showMessage(context.getString(R.string.deadline_cant_be_earlier_than_current_time))
                }

                EditTaskStore.Label.EditDeadlineClickedAndReminderIsIncorrect -> {
                    snackBarManager.showMessage(context.getString(R.string.time_of_reminder_is_incorrect))
                }
            }
        }.launchIn(rememberCoroutineScope)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FAB(component)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = TrackerNewTheme.colors.linearGradientBackground)
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                OutlinedTextFieldName(
                    state = state,
                    onValueChange = {
                        component.onNameChanged(it)
                    }
                )

                OutlinedTextFieldDescription(
                    state = state,
                    onValueChange = {
                        component.onDescriptionChanged(it)
                    }
                )

                OutlinedTextFieldCategoryWithMenu(
                    state = state,
                    component = component,
                    onValueChange = {
                        component.onCategoryChanged(it)
                    }
                )

                val stateDateAndTimePicker = remember {
                    mutableStateOf(false)
                }
                OutlinedTextFieldDeadline(
                    state = state,
                    onValueChange = {
                        component.onDeadlineChanged(it.toLong())
                    },
                    onClick = {
                        stateDateAndTimePicker.value = true
                    }
                )

                AlarmEnable(
                    state = state,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    onCheckedChange = {
                        component.onChangeAlarmEnableClicked()
                    },
                    onTimesCountMenuItemClick = {
                        component.onChangeTimesCountClicked(it)
                    },
                    onTimeForDeadlineMenuItemClick = {
                        component.onChangeTimeForDeadlineClicked(it)
                    },
                )

                val stateSubTaskDialog = remember {
                    mutableStateOf(false)
                }
                SubTasks(
                    modifier = Modifier
                        .padding(8.dp),
                    state = state,
                    onAddSubTaskClick = {
                        stateSubTaskDialog.value = true
                    },
                    onDeleteSubTaskClick = {
                        component.onDeleteSubTaskClicked(it)
                    },
                    onSubTaskClick = {
                        component.onSubTaskChangeStatusClicked(it)
                    }
                )

                AddSubTaskDialog(
                    stateDialog = stateSubTaskDialog,
                    state = state,
                    onDismiss = {
                        stateSubTaskDialog.value = false
                    },
                    onCancelClick = {
                        stateSubTaskDialog.value = false
                    },
                    onAddClick = {
                        component.onAddSubTaskClicked()
                    },
                    onValueChange = {
                        component.onSubTaskNameChanged(it)
                    }
                )

                TaskCompletedStatus(
                    modifier = Modifier
                        .padding(8.dp),
                    state = state,
                    onClick = {
                        component.onChangeStatusClicked()
                    }
                )

                DateAndTimePickerDialogEditScreen(
                    state = stateDateAndTimePicker,
                    initialDateMillis = state.deadline,
                    onDateTimeSelected = {
                        component.onDeadlineChanged(it)
                        stateDateAndTimePicker.value = false
                    },
                    onDismiss = {
                        stateDateAndTimePicker.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FAB(component: EditTaskComponent) {
    OutlinedButton(
        modifier = Modifier.imePadding(),
        onClick = {
            component.onEditTaskClicked()
        }
    ) {
        Text(
            text = stringResource(R.string.confirm),
            color = TrackerNewTheme.colors.textColor
        )
    }
}

@Composable
fun OutlinedTextFieldName(
    modifier: Modifier = Modifier,
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = state.name,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = stringResource(R.string.title),
                color = TrackerNewTheme.colors.textColor
            )
        },
        supportingText = {
            Text(
                text = stringResource(R.string.required),
                color = if (state.name.isNotEmpty()) Green200 else Color.Red,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldCategory(
    state: EditTaskStore.State,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClick()
            }
            .then(modifier),
        value = state.category,
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = stringResource(R.string.category),
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldCategoryWithMenu(
    state: EditTaskStore.State,
    component: EditTaskComponent,
    onValueChange: (String) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Menu(
        expanded = expanded,
        items = state.categories.map { it.name },
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            component.onCategoryChanged(it)
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldCategory(
                modifier = modifier,
                state = state,
                onClick = {
                    expanded.value = !expanded.value
                },
                onValueChange = {
                    onValueChange(it)
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    modifier: Modifier = Modifier,
    expanded: State<Boolean>,
    items: List<String>,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded.value,
        onExpandedChange = {
        }
    ) {
        content(Modifier.menuAnchor())

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = TrackerNewTheme.colors.onBackground)
                .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp / 3).dp),
            expanded = expanded.value,
            onDismissRequest = {
                onDismissRequest()
            }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it,
                            color = TrackerNewTheme.colors.textColor
                        )
                    },
                    onClick = {
                        onItemClick(it)
                    }
                )
            }
        }
    }
}

@Composable
fun OutlinedTextFieldDescription(
    modifier: Modifier = Modifier,
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = state.description,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = stringResource(R.string.description),
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldDeadline(
    modifier: Modifier = Modifier,
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClick()
            },
        value = state.deadline.toDateString(),
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = stringResource(R.string.deadline),
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndTimePickerDialogEditScreen(
    state: State<Boolean>,
    initialDateMillis: Long,
    onDateTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    if (!state.value) return

    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }

    val initialDate =
        if (initialDateMillis == 0L) System.currentTimeMillis()
        else initialDateMillis

    var selectedDate by remember { mutableLongStateOf(initialDate) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate,
    )

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = initialDate.toLocalDateTime().hour,
        initialMinute = initialDate.toLocalDateTime().minute
    )

    if (showDatePicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors().copy(
                containerColor = TrackerNewTheme.colors.background
            ),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                            showDatePicker = false
                            showTimePicker = true
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = getDatePickerColors()
            )
        }
    }

    if (showTimePicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors().copy(
                containerColor = TrackerNewTheme.colors.background
            ),
            onDismissRequest = {
                showTimePicker = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedLocalDate = Instant.ofEpochMilli(selectedDate)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        val selectedDateTime = selectedLocalDate
                            .atTime(timePickerState.hour, timePickerState.minute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()

                        onDateTimeSelected(selectedDateTime)
                        showTimePicker = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.select),
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        showDatePicker = true
                    }
                ) {
                    Text(
                        text = stringResource(R.string.backward),
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(
                    state = timePickerState,
                    colors = getTimePickerColors()
                )
            }
        }
    }
}


@Composable
fun AlarmEnable(
    state: EditTaskStore.State,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit,
    onTimesCountMenuItemClick: (Int) -> Unit,
    onTimeForDeadlineMenuItemClick: (String) -> Unit
) {
    val expandedTime = remember {
        mutableStateOf(false)
    }
    val expandedTimesCount = remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.reminder),
                color = TrackerNewTheme.colors.textColor,
                fontSize = 16.sp
            )
            Switch(
                checked = state.alarmEnable,
                onCheckedChange = {
                    onCheckedChange()
                }
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            visible = state.alarmEnable,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.remind_for),
                    color = TrackerNewTheme.colors.textColor,
                    fontSize = 16.sp
                )
                Menu(
                    expanded = expandedTimesCount,
                    items = listFromOneToOneHundred,
                    onDismissRequest = {
                        expandedTimesCount.value = false
                    },
                    onItemClick = {
                        onTimesCountMenuItemClick(it.toInt())
                        expandedTimesCount.value = false
                    },
                    content = {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    expandedTimesCount.value = !expandedTimesCount.value
                                }
                                .then(it),
                            text = state.timeUnitCount.toString(),
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 16.sp
                        )
                    }
                )
                Menu(
                    expanded = expandedTime,
                    items = listOfTimeUnits,
                    onDismissRequest = {
                        expandedTime.value = false
                    },
                    onItemClick = {
                        onTimeForDeadlineMenuItemClick(it)
                        expandedTime.value = false
                    },
                    content = {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    expandedTime.value = !expandedTime.value
                                }
                                .then(it),
                            text = state.timeUnit,
                            color = TrackerNewTheme.colors.textColor,
                            fontSize = 16.sp
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun SubTasks(
    modifier: Modifier = Modifier,
    state: EditTaskStore.State,
    onAddSubTaskClick: () -> Unit,
    onDeleteSubTaskClick: (Int) -> Unit,
    onSubTaskClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .then(modifier)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Black300,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
                },
            text = stringResource(R.string.subtasks),
            color = TrackerNewTheme.colors.textColor
        )
        LazyColumn(
            modifier = Modifier
                .heightIn(0.dp, (LocalConfiguration.current.screenHeightDp / 5).dp)
        ) {
            items(
                items = state.subTasks,
                key = { it.id }
            ) { subTask ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                        .clickable {
                            onSubTaskClick(subTask.id)
                        }
                ) {
                    Text(
                        color = if (subTask.isCompleted) Green200 else Red300,
                        text = subTask.name
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .clickable {
                                onDeleteSubTaskClick(subTask.id)
                            },
                        painter = painterResource(R.drawable.delete_outline_24),
                        contentDescription = null,
                        tint = TrackerNewTheme.colors.tintColor
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 4.dp, bottom = 4.dp)
                .clickable {
                    onAddSubTaskClick()
                }
        ) {
            Text(
                text = stringResource(R.string.add),
                fontFamily = FontFamily.Serif,
                color = TrackerNewTheme.colors.textColor
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubTaskDialog(
    stateDialog: State<Boolean>,
    state: EditTaskStore.State,
    onDismiss: () -> Unit,
    onCancelClick: () -> Unit,
    onAddClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    if (!stateDialog.value) return

    DatePickerDialog(
        colors = DatePickerDefaults.colors().copy(
            containerColor = TrackerNewTheme.colors.background
        ),
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAddClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.add),
                    color = TrackerNewTheme.colors.textColor
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onCancelClick()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = TrackerNewTheme.colors.textColor
                )
            }
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.subtask),
                    color = TrackerNewTheme.colors.textColor
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(8.dp),
                    value = state.subTask,
                    onValueChange = {
                        onValueChange(it)
                    },
                    label = {
                        Text(text = stringResource(R.string.title))
                    },
                    supportingText = {
                        Text(
                            text = stringResource(R.string.required),
                            color = if (state.subTask.isNotEmpty()) Green200 else Color.Red,
                            fontSize = 12.sp
                        )
                    },
                    colors = getOutlinedTextFieldColors()
                )
            }
        }
    }
}

@Composable
fun TaskCompletedStatus(
    modifier: Modifier = Modifier,
    state: EditTaskStore.State,
    onClick: () -> Unit
) {
    val text = when (state.status) {
        TaskStatus.Completed -> stringResource(R.string.completed)
        TaskStatus.Executed -> stringResource(R.string.executed)
        TaskStatus.Failed -> stringResource(R.string.failed)
        TaskStatus.InTheProcess -> stringResource(R.string.in_the_process)
    }
    val color = when (state.status) {
        TaskStatus.Completed -> TrackerNewTheme.colors.oppositeColor
        TaskStatus.Executed -> Green200
        TaskStatus.Failed -> Red300
        TaskStatus.InTheProcess -> Orange100
    }
    val icon = when (state.status) {
        TaskStatus.Completed -> {
            R.drawable.question_24
        }

        TaskStatus.Executed -> {
            R.drawable.done_24
        }

        TaskStatus.Failed -> {
            R.drawable.not_completed_24
        }

        TaskStatus.InTheProcess -> {
            R.drawable.dots_24
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .then(modifier)
    ) {
        Text(
            text = text,
            color = color
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color
        )
    }
}
