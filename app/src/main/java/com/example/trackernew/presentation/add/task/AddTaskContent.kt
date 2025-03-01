package com.example.trackernew.presentation.add.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddTaskContent() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            OutlinedTextFieldName {

            }
            OutlinedTextFieldDescription {

            }

            val stateVisibleDatePicker = remember {
                mutableStateOf(false)
            }
            val stateVisibleTimePicker = remember {
                mutableStateOf(false)
            }

            OutlinedTextFieldDeadline(
                onValueChange = {

                },
                onClick = {
                    stateVisibleDatePicker.value = true
                }
            )

            val dateInMillis = remember {
                mutableLongStateOf(0)
            }

            val datePickerState = rememberDatePickerState()
            val timePickerState = rememberTimePickerState()

            DateAndTimePicker(
                stateVisibleDatePicker = stateVisibleDatePicker,
                stateVisibleTimePicker = stateVisibleTimePicker,
                datePickerState = datePickerState,
                timePickerState = timePickerState,
                onContinueClick = {
                    dateInMillis.value += datePickerState.selectedDateMillis ?: 0
                    stateVisibleDatePicker.value = false
                    stateVisibleTimePicker.value = true
                },
                onSelectClick = {
                    dateInMillis.value +=
                        (timePickerState.hour * 60 + timePickerState.minute) * 60 * 1000L
                    stateVisibleTimePicker.value = false
                },
                onCancelClick = {
                    stateVisibleDatePicker.value = false
                    stateVisibleTimePicker.value = false
                },
                onDismiss = {
                    stateVisibleTimePicker.value = false
                    stateVisibleDatePicker.value = false
                },
            )
        }
    }
}

@Composable
fun OutlinedTextFieldName(
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Название")
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldDescription(
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Описание")
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}

@Composable
fun OutlinedTextFieldDeadline(
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
            Text(text = "Дедлайн")
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndTimePicker(
    stateVisibleDatePicker: State<Boolean>,
    stateVisibleTimePicker: State<Boolean>,
    datePickerState: DatePickerState,
    timePickerState: TimePickerState,
    onContinueClick: () -> Unit,
    onSelectClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
) {

    MyDatePicker(
        stateVisibleDatePicker = stateVisibleDatePicker,
        datePickerState = datePickerState,
        onContinueClick = onContinueClick,
        onCancelClick = onCancelClick,
        onDismiss = onDismiss
    )

    MyTimePicker(
        stateVisibleTimePicker = stateVisibleTimePicker,
        timePickerState = timePickerState,
        onSelectClick = onSelectClick,
        onCancelClick = onCancelClick,
        onDismiss = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    stateVisibleDatePicker: State<Boolean>,
    datePickerState: DatePickerState,
    onContinueClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
    ) {
    if (stateVisibleDatePicker.value){
        DatePickerDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onContinueClick()
                    }
                ) {
                    Text("Продолжить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancelClick()
                    }
                ) {
                    Text("Отменить")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker(
    stateVisibleTimePicker: State<Boolean>,
    timePickerState: TimePickerState,
    onSelectClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (stateVisibleTimePicker.value) {
        DatePickerDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSelectClick()
                    }
                ) {
                    Text("Выбрать")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancelClick()
                    }
                ) {
                    Text("Отменить")
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(
                    state = timePickerState
                )
            }
        }
    }
}