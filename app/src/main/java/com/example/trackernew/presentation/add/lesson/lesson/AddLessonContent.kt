package com.example.trackernew.presentation.add.lesson.lesson

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.trackernew.presentation.add.task.Menu
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors


@Composable
fun AddLessonContent(component: AddLessonComponent, snackbarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    component.onSaveLessonClicked()
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

            OutlinedTextFieldLessonNameWithMenu(
                state = state,
                component = component,
                onValueChange = {
                    component.onNameChanged(it)
                },
                onTrailingIconClick = {
                    component.goToAddNameLessonContent()
                }
            )

            OutlinedTextFieldLecturerWithMenu(
                state = state,
                component = component,
                onValueChange = {
                    component.onLecturerChanged(it)
                },
                onTrailingIconClick = {
                    component.goToAddLecturerContent()
                }
            )

            OutlinedTextFieldAudienceWithMenu(
                state = state,
                component = component,
                onValueChange = {
                    component.onAudienceChanged(it)
                },
                onTrailingIconClick = {
                    component.goToAddAudienceContent()
                }
            )
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
                    if (state.lessonNames.isEmpty()) {
                        component.goToAddNameLessonContent()
                    } else {
                        expanded.value = true
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
                text = "Название занятия",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.lessonName,
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
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
                    if (state.lecturers.isEmpty()) {
                        component.goToAddLecturerContent()
                    } else {
                        expanded.value = true
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
                text = "Преподаватель",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.lecturer,
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
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
                    if (state.audiences.isEmpty()) {
                        component.goToAddAudienceContent()
                    } else {
                        expanded.value = true
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
                text = "Аудитория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.audience,
        onValueChange = {
            onValueChange(it)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    },
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    )
}
