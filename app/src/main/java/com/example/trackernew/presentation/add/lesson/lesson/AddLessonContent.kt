package com.example.trackernew.presentation.add.lesson.lesson

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trackernew.presentation.extensions.toLocalDateTime
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getDatePickerColors
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import com.example.trackernew.ui.theme.getTimePickerColors
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun AddLessonContent(component: AddLessonComponent) {
    val state by component.model.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    component.onSaveLessonClicked()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .height(72.dp)
            )
            OutlinedTextFieldName(
                state = state,
                onValueChange = {
                    component.onNameChanged(it)
                },
                onClick = {
                    Log.d("TEST", "Click name lesson")
                }
            )
            val stateDateAndTimePicker = remember {
                mutableStateOf(false)
            }
            DateAndTimePickerDialog(
                state = stateDateAndTimePicker,
                onDateTimeSelected = {
                    // TODO component.onDeadlineChanged(it)
                    stateDateAndTimePicker.value = false
                },
                onDismiss = {
                    stateDateAndTimePicker.value = false
                }
            )
            OutlinedTextFieldLecturer(
                {},
                {}
            )
            OutlinedTextFieldAudience(
                {},
                {}
            )
            OutlinedTextFieldTypeOfLesson(
                {},
                {}
            )
        }
    }
}

@Composable
fun OutlinedTextFieldName(
    state: AddLessonStore.State,
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
fun OutlinedTextFieldLecturer(
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
        label = {
            Text(
                text = "Преподаватель"
            )
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldAudience(
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
        label = {
            Text(
                text = "Аудитория"
            )
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldTypeOfLesson(
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
        label = {
            Text(
                text = "Тип занятия"
            )
        },
        value = "",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    expanded: State<Boolean>,
    items: List<String>,
    onDismissRequest: () -> Unit,
    onItemClick: () -> Unit,
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
                .fillMaxWidth(),
            expanded = expanded.value,
            onDismissRequest = {
                onDismissRequest()
            }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        onItemClick()
                    }
                )
            }
        }
    }
}