package com.example.trackernew.presentation.add.task

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green300
import com.example.trackernew.ui.theme.Red300
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getDatePickerColors
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun AddTaskContent(component: AddTaskComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                AddTaskStore.Label.TaskSaved -> {
                    snackBarManager.showMessage(context.getString(R.string.task_saved))
                }

                AddTaskStore.Label.SubTaskSaved -> {
                    snackBarManager.showMessage(context.getString(R.string.subtask_saved))
                }

                AddTaskStore.Label.CategoriesListIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.list_of_categories_is_empty))
                }

                AddTaskStore.Label.AddTaskClickedAndNameIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }

                AddTaskStore.Label.AddSubTaskClickedAndNameIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }

                AddTaskStore.Label.AddDeadlineClickedAndDeadlineIsIncorrect -> {
                    snackBarManager.showMessage(context.getString(R.string.deadline_cant_be_earlier_than_current_time))
                }

                AddTaskStore.Label.AddTaskClickedAndDeadlineIsIncorrect -> {
                    snackBarManager.showMessage(context.getString(R.string.deadline_cant_be_earlier_than_current_time))
                }
            }
        }.launchIn(rememberCoroutineScope)
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FAB(component)
        },
        containerColor = TrackerNewTheme.colors.background
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
                val stateDateAndTimePicker = remember {
                    mutableStateOf(false)
                }
                val stateAddSubTask = remember {
                    mutableStateOf(false)
                }
                LazyColumn {
                    item {
                        OutlinedTextFieldName(
                            state = state,
                            onValueChange = {
                                component.onNameChanged(it)
                            },
                            onClearIconClick = {
                                component.onClearNameClicked()
                            }
                        )
                    }
                    item {
                        OutlinedTextFieldDescription(
                            state = state,
                            onValueChange = {
                                component.onDescriptionChanged(it)
                            },
                            onClearIconClick = {
                                component.onClearDescriptionClicked()
                            }
                        )
                    }
                    item {
                        OutlinedTextFieldCategoryWithMenu(
                            state = state,
                            component = component,
                            onValueChange = {
                                component.onCategoryChanged(it)
                            },
                            onClearIconClick = {
                                component.onClearCategoryClicked()
                            }
                        )
                    }
                    item {
                        OutlinedTextFieldDeadline(
                            state = state,
                            onValueChange = {
                                component.onDeadlineChanged(it.toLong())
                            },
                            onClearIconClick = {
                                component.onClearDeadlineClicked()
                            },
                            onClick = {
                                stateDateAndTimePicker.value = true
                            }
                        )
                    }
                    item {
                        AlarmEnable(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            state = state,
                            onCheckedChange = {
                                component.onChangeAlarmEnableClicked()
                            }
                        )
                    }
                    item {
                        SubTasks(
                            state = state,
                            onAddSubTaskClick = {
                                stateAddSubTask.value = true
                            },
                            onDeleteSubTaskClick = {
                                component.onDeleteSubTaskClicked(it)
                            }
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(72.dp)
                        )
                    }
                }

                AddSubTaskDialog(
                    state = state,
                    stateDialog = stateAddSubTask,
                    onDismiss = {
                        stateAddSubTask.value = false
                    },
                    onValueChange = {
                        component.onSubTaskNameChanged(it)
                    },
                    onCancelClick = {
                        stateAddSubTask.value = false
                    },
                    onAddClick = {
                        component.onAddSubTaskClicked()
                    },
                )

                DateAndTimePickerDialog(
                    state = stateDateAndTimePicker,
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
private fun FAB(
    component: AddTaskComponent,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = Modifier
            .imePadding()
            .then(modifier),
        onClick = {
            component.onAddTaskClicked()
        },
        containerColor = TrackerNewTheme.colors.onBackground,
        contentColor = TrackerNewTheme.colors.oppositeColor
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = TrackerNewTheme.colors.tintColor
        )
    }
}

@Composable
fun OutlinedTextFieldName(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onClearIconClick: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
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
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onClearIconClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        supportingText = {
            Text(
                text = stringResource(R.string.required),
                color = if (state.name.isNotEmpty()) Green300 else Red300,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldDescription(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onClearIconClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        value = state.description,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = "Описание",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onClearIconClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldCategory(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onClearIconClick: () -> Unit,
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
                text = "Категория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onClearIconClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldCategoryWithMenu(
    component: AddTaskComponent,
    state: AddTaskStore.State,
    onClearIconClick: () -> Unit,
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
                    when (state.categories.isNotEmpty()) {
                        true -> {
                            expanded.value = true
                        }

                        false -> {
                            component.onCategoryClickedAndCategoriesListIsEmpty()
                        }
                    }
                },
                onValueChange = {
                    onValueChange(it)
                },
                onClearIconClick = {
                    onClearIconClick()
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    expanded: State<Boolean>,
    items: List<String>,
    onDismissRequest: () -> Unit,
    onItemClick: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
        }
    ) {
        content(Modifier.menuAnchor())

        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(TrackerNewTheme.colors.onBackground),
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
fun OutlinedTextFieldDeadline(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onValueChange: (String) -> Unit,
    onClearIconClick: () -> Unit,
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
            }
            .then(modifier),
        value = state.deadline.toDateString(),
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Дедлайн",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onClearIconClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndTimePickerDialog(
    state: State<Boolean>,
    initialDateMillis: Long? = null,
    onDateTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    if (!state.value) return

    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedDate by remember {
        mutableLongStateOf(
            initialDateMillis ?: System.currentTimeMillis()
        )
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate,
        yearRange = IntRange(2020, 2030)
    )

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = initialDateMillis?.toLocalDateTime()?.hour ?: LocalTime.now().hour,
        initialMinute = initialDateMillis?.toLocalDateTime()?.minute ?: LocalTime.now().minute
    )

    if (showDatePicker) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors().copy(
                containerColor = TrackerNewTheme.colors.background,
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
                colors = getDatePickerColors(),
                state = datePickerState
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
    state: AddTaskStore.State,
    modifier: Modifier = Modifier,
    onCheckedChange: () -> Unit
) {
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
}

@Composable
fun SubTasks(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onAddSubTaskClick: () -> Unit,
    onDeleteSubTaskClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val lineColor = TrackerNewTheme.colors.oppositeColor

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = lineColor,
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
                ) {
                    Text(
                        color = if (subTask.isCompleted) Green300 else Red300,
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
                imageVector = Icons.Filled.Add,
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
    state: AddTaskStore.State,
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
                            color = if (state.subTask.isNotEmpty()) Green300 else Red300,
                            fontSize = 12.sp
                        )
                    },
                    colors = getOutlinedTextFieldColors()
                )
            }
        }
    }
}
