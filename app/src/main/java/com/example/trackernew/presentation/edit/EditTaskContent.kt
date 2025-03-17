package com.example.trackernew.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.trackernew.R
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.presentation.utils.ADD
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.Red
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getDatePickerColors
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import java.time.ZoneId

@Composable
fun EditTaskContent(component: EditTaskComponent) {
    val state by component.model.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            OutlinedButton(
                onClick = {
                    component.onEditTaskClicked()
                }
            ) {
                Text(
                    text = "Подтвердить",
                    color = TrackerNewTheme.colors.textColor
                )
            }
        },
        containerColor = TrackerNewTheme.colors.background
    ) { paddingValues ->
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

            val stateSubTaskDialog = remember {
                mutableStateOf(false)
            }

            SubTasks(
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
                state = state,
                onClick = {
                    component.onChangeCompletedStatusClick()
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

@Composable
fun OutlinedTextFieldName(
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(
                text = "Название",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.name,
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldCategory(
    state: EditTaskStore.State,
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        readOnly = true,
        label = {
            Text(
                text = "Категория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onIconClick()
                    },
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        value = state.category,
        onValueChange = {
            onValueChange(it)
        }
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
                onIconClick = {
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
                .background(color = TrackerNewTheme.colors.onBackground),
            expanded = expanded.value,
            onDismissRequest = {
                onDismissRequest()
            },

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
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(
                text = "Описание",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.description,
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldDeadline(
    state: EditTaskStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        readOnly = true,
        enabled = false,
        colors = getOutlinedTextFieldColors(),
        label = {
            Text(
                text = "Дедлайн",
                color = TrackerNewTheme.colors.textColor
            )
        },
        value = state.deadline.toDateString(),
        onValueChange = {
            onValueChange(it)
        }
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
                        "Далее",
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        "Отмена",
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
                        val selectedLocalDateTime = (datePickerState.selectedDateMillis ?: 0)
                            .toLocalDateTime()

                        val finalDateTime = selectedLocalDateTime
                            .withHour(timePickerState.hour)
                            .withMinute(timePickerState.minute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                        onDateTimeSelected(finalDateTime)
                        showTimePicker = false
                    }
                ) {
                    Text(
                        "Выбрать",
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    showDatePicker = true
                }) {
                    Text(
                        "Назад",
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
fun SubTasks(
    state: EditTaskStore.State,
    onAddSubTaskClick: () -> Unit,
    onDeleteSubTaskClick: (Int) -> Unit,
    onSubTaskClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
                },
            text = "Подзадачи",
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
                        color = if (subTask.isCompleted) Green else Red,
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
                text = ADD,
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
                    text = "Добавить",
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
                    text = "Отменить",
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
                    text = "Подзадача",
                    color = TrackerNewTheme.colors.textColor
                )
                OutlinedTextField(
                    modifier = Modifier
                        .padding(8.dp),
                    value = state.subTask,
                    onValueChange = {
                        onValueChange(it)
                    },
                    colors = getOutlinedTextFieldColors()
                )
            }

        }
    }
}

@Composable
fun TaskCompletedStatus(
    state: EditTaskStore.State,
    onClick: () -> Unit
) {
    val text = when (state.isCompleted) {
        true -> "Выполнен"
        false -> "В процессе"
    }
    val color = when (state.isCompleted) {
        true -> Green
        false -> Red
    }
    val icon = when (state.isCompleted) {
        true -> R.drawable.done_24
        false -> R.drawable.not_completed_24
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick()
            }
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
