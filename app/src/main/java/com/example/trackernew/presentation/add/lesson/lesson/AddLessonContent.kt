package com.example.trackernew.presentation.add.lesson.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trackernew.domain.entity.lessons
import com.example.trackernew.presentation.add.task.Menu
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.presentation.extensions.toTimeString
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green200
import com.example.trackernew.ui.theme.Red300
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalTime


@Composable
fun AddLessonContent(component: AddLessonComponent, snackbarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()


    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                AddLessonStore.Label.AudiencesListIsEmpty -> {
                    /** Nothing **/
                }

                AddLessonStore.Label.LecturersListIsEmpty -> {
                    /** Nothing **/
                }

                AddLessonStore.Label.NameLessonsListIsEmpty -> {
                    /** Nothing **/
                }

                AddLessonStore.Label.LessonSaved -> {
                    snackbarManager.showMessage("Занятие сохранено")
                }

                AddLessonStore.Label.AddLessonClickedAndLessonNameIsEmpty -> {
                    snackbarManager.showMessage("Название не должно быть пустым")
                }

                AddLessonStore.Label.EndTimeSaveClickedAndItsLessThanStartTime -> {
                    snackbarManager.showMessage("Конец не может наступить раньше начала")
                }

                AddLessonStore.Label.StartTimeSaveClickedAndItsMoreThanEndTime -> {
                    snackbarManager.showMessage("Старт не может быть позже конца")
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
                    component.onAddLessonClicked()
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
                OutlinedTextFieldLessonNameWithMenu(
                    state = state,
                    component = component,
                    onValueChange = {
                        component.onNameChanged(it)
                    },
                    onTrailingIconClick = {
                        component.onLessonNameClickedAndLessonNamesListIsEmpty()
                    }
                )

                OutlinedTextFieldLecturerWithMenu(
                    state = state,
                    component = component,
                    onValueChange = {
                        component.onLecturerChanged(it)
                    },
                    onTrailingIconClick = {
                        component.onLecturerClickedAndLecturersListIsEmpty()
                    }
                )

                OutlinedTextFieldAudienceWithMenu(
                    state = state,
                    component = component,
                    onValueChange = {
                        component.onAudienceChanged(it)
                    },
                    onTrailingIconClick = {
                        component.onAudienceClickedAndAudiencesListIsEmpty()
                    }
                )

                val stateStartTimePicker = remember {
                    mutableStateOf(false)
                }
                OutlinedTextFieldStart(
                    state = state,
                    onValueChange = {
                    },
                    onClick = {
                        stateStartTimePicker.value = true
                    }
                )

                val stateEndTimePicker = remember {
                    mutableStateOf(false)
                }
                OutlinedTextFieldEnd(
                    state = state,
                    onValueChange = {
                    },
                    onClick = {
                        stateEndTimePicker.value = true
                    }
                )

                OutlinedTextFieldTypeOfLessonWithMenu(
                    state = state,
                    component = component,
                    onValueChange = {
                        component.onTypeOfLessonChanged(it)
                    }
                )

                TimePickerDialog(
                    state = stateStartTimePicker,
                    onDateTimeSelected = {
                        component.onStartChanged(it)
                        stateStartTimePicker.value = false
                    },
                    onClose = {
                        stateStartTimePicker.value = false
                    },
                    onDismiss = {
                        stateStartTimePicker.value = false
                    }
                )

                TimePickerDialog(
                    state = stateEndTimePicker,
                    onDateTimeSelected = {
                        component.onEndChanged(it)
                        stateEndTimePicker.value = false
                    },
                    onClose = {
                        stateEndTimePicker.value = false
                    },
                    onDismiss = {
                        stateEndTimePicker.value = false
                    }
                )
            }
        }
    }
}

@Composable
fun OutlinedTextFieldLessonNameWithMenu(
    component: AddLessonComponent,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    Menu(
        expanded = expanded,
        items = state.lessonNames.map { it.name },
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            component.onNameChanged(it)
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldLessonName(
                modifier = modifier,
                state = state,
                onClick = {
                    when (state.lessonNames.isNotEmpty()) {
                        true -> {
                            expanded.value = true
                        }

                        false -> {
                            component.onLessonNameClickedAndLessonNamesListIsEmpty()
                        }
                    }
                },
                onValueChange = {
                    onValueChange(it)
                },
                onTrailingIconClick = {
                    onTrailingIconClick()
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldLessonName(
    modifier: Modifier = Modifier,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onTrailingIconClick: () -> Unit
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
        value = state.lessonName,
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Название занятия",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrackerNewTheme.colors.oppositeColor
            )
        },
        supportingText = {
            Text(
                text = "*Обязательно",
                color = if (state.lessonName.isNotEmpty()) Green200 else Red300,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldTypeOfLessonWithMenu(
    component: AddLessonComponent,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Menu(
        expanded = expanded,
        items = lessons,
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            component.onTypeOfLessonChanged(it)
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldTypeOfLesson(
                modifier = modifier,
                state = state,
                onClick = {
                    expanded.value = true
                },
                onValueChange = {
                    onValueChange(it)
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldTypeOfLesson(
    modifier: Modifier = Modifier,
    state: AddLessonStore.State,
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
        value = state.typeOfLesson,
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Тип занятия",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldLecturerWithMenu(
    component: AddLessonComponent,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Menu(
        expanded = expanded,
        items = state.lecturers.map { it.name },
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            component.onLecturerChanged(it)
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldLecturer(
                modifier = modifier,
                state = state,
                onClick = {
                    when (state.lecturers.isNotEmpty()) {
                        true -> {
                            expanded.value = true

                        }

                        false -> {
                            component.onLecturerClickedAndLecturersListIsEmpty()

                        }
                    }
                },
                onValueChange = {
                    onValueChange(it)
                },
                onTrailingIconClick = {
                    onTrailingIconClick()
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldLecturer(
    modifier: Modifier = Modifier,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onTrailingIconClick: () -> Unit
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
        value = state.lecturer,
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Преподаватель",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrackerNewTheme.colors.oppositeColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}


@Composable
fun OutlinedTextFieldAudienceWithMenu(
    component: AddLessonComponent,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit
) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Menu(
        expanded = expanded,
        items = state.audiences.map { it.name },
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            component.onAudienceChanged(it)
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldAudience(
                modifier = modifier,
                state = state,
                onClick = {
                    when (state.audiences.isNotEmpty()) {
                        true -> {
                            expanded.value = true
                        }

                        false -> {
                            component.onAudienceClickedAndAudiencesListIsEmpty()
                        }
                    }
                },
                onValueChange = {
                    onValueChange(it)
                },
                onTrailingIconClick = {
                    onTrailingIconClick()
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldAudience(
    modifier: Modifier = Modifier,
    state: AddLessonStore.State,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onTrailingIconClick: () -> Unit
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
        value = state.audience,
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Аудитория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrackerNewTheme.colors.oppositeColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldStart(
    state: AddLessonStore.State,
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
        value = state.start.toTimeString(),
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Начало",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@Composable
fun OutlinedTextFieldEnd(
    state: AddLessonStore.State,
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
        value = state.end.toTimeString(),
        onValueChange = {
            onValueChange(it)
        },
        enabled = false,
        readOnly = true,
        label = {
            Text(
                text = "Конец",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: State<Boolean>,
    initialDateMillis: Long? = null,
    onDateTimeSelected: (Long) -> Unit,
    onClose: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!state.value) return
    var showTimePicker by remember { mutableStateOf(true) }

    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = initialDateMillis?.toLocalDateTime()?.hour ?: LocalTime.now().hour,
        initialMinute = initialDateMillis?.toLocalDateTime()?.minute ?: LocalTime.now().minute
    )

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
                        showTimePicker = false
                        val time =
                            timePickerState.hour * 60 * 60 * 1000L + timePickerState.minute * 60 * 1000L
                        onDateTimeSelected(time)
                        onClose()
                    }
                ) {
                    Text(
                        text = "Выбрать",
                        color = TrackerNewTheme.colors.textColor
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        onClose()
                    }
                ) {
                    Text(
                        text = "Отмена",
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