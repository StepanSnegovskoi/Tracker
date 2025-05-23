package com.example.trackernew.presentation.add.task

import android.util.Log
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.presentation.extensions.toDateString
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.presentation.utils.ADD
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.Red
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getDatePickerColors
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun AddTaskContent(component: AddTaskComponent, snackbarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when(it){
                AddTaskStore.Label.CategoriesClickedAndTheyAreEmpty -> {
                    snackbarManager.showMessage("Список категорий пуст")
                }
                AddTaskStore.Label.TaskSaved -> {
                    snackbarManager.showMessage("Задача сохранена")
                }

                AddTaskStore.Label.SaveTaskClickedAndNameIsEmpty -> {
                    snackbarManager.showMessage("Название не должно быть пустым")
                }

                AddTaskStore.Label.AddSubTaskClickedAndNameIsEmpty -> {
                    snackbarManager.showMessage("Название не должно быть пустым")
                }

                AddTaskStore.Label.SubTaskSaved -> {
                    snackbarManager.showMessage("Подзадача добавлена")
                }
            }
        }.launchIn(rememberCoroutineScope)
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    component.onSaveTaskClicked()
                },
                containerColor = TrackerNewTheme.colors.onBackground,
                contentColor = TrackerNewTheme.colors.oppositeColor
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
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

            val stateAddSubTask = remember {
                mutableStateOf(false)
            }

            SubTasks(
                state = state,
                onAddSubTaskClick = {
                    stateAddSubTask.value = true
                },
                onDeleteSubTaskClick = {
                    component.onDeleteSubTaskClicked(it)
                }
            )

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

@Composable
fun OutlinedTextFieldName(
    state: AddTaskStore.State,
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
        },
        supportingText = {
            Text(
                text = "*Обязательно",
                color = if(state.name.isNotEmpty()) Color.Green else Color.Red,
                fontSize = 12.sp
            )
        }
    )
}

@Composable
fun OutlinedTextFieldDescription(
    state: AddTaskStore.State,
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
fun OutlinedTextFieldCategory(
    modifier: Modifier = Modifier,
    state: AddTaskStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick()
            }
            .then(modifier),
        readOnly = true,
        enabled = false,
        label = {
            Text(
                text = "Категория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.category,
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldCategoryWithMenu(
    component: AddTaskComponent,
    state: AddTaskStore.State,
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
                    if (state.categories.isEmpty()) {
                        component.ifCategoriesAreEmpty()
                    } else {
                        expanded.value = true
                    }
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
    state: AddTaskStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
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
                        text = "Далее",
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Отмена",
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
                        val selectedLocalDateTime = (datePickerState.selectedDateMillis ?: 0)
                            .toLocalDateTime()

                        val finalDateTime = selectedLocalDateTime
                            .withHour(timePickerState.hour)
                            .withMinute(timePickerState.minute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                        onDateTimeSelected(finalDateTime)
                        Log.d("TEST_TEST", finalDateTime.toString())
                        showTimePicker = false
                    }
                ) {
                    Text(
                        text = "Выбрать",
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
                        text = "Назад",
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
    state: AddTaskStore.State,
    onAddSubTaskClick: () -> Unit,
    onDeleteSubTaskClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
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
                    label = {
                        Text(text = "Название")
                    },
                    colors = getOutlinedTextFieldColors(),
                    supportingText = {
                        Text(
                            text = "*Обязательно",
                            color = if(state.subTask.isNotEmpty()) Green else Color.Red,
                            fontSize = 12.sp
                        )
                    }
                )
            }

        }
    }
}
