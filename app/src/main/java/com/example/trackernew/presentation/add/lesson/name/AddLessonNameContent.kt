package com.example.trackernew.presentation.add.lesson.name

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green200
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AddLessonNameContent(component: AddLessonNameComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                AddLessonNameStore.Label.LessonNameSaved -> {
                    snackBarManager.showMessage(context.getString(R.string.name_of_lesson_saved))
                }

                AddLessonNameStore.Label.AddLessonNameClickedAndNameIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }
            }
        }.launchIn(rememberCoroutineScope)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = TrackerNewTheme.colors.background,
        floatingActionButton = {
            FAB(component)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = TrackerNewTheme.colors.linearGradientBackground)
        ) {
            OutlinedTextFieldCategory(
                state = state,
                modifier = Modifier
                    .padding(paddingValues),
                onValueChange = {
                    component.onLessonNameChanged(it)
                },
                onClearIconClick = {
                    component.onClearLessonNameClicked()
                }
            )
        }
    }
}

@Composable
private fun FAB(
    component: AddLessonNameComponent,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = Modifier
            .imePadding()
            .then(modifier),
        onClick = {
            component.onAddLessonNameClicked()
        },
        containerColor = TrackerNewTheme.colors.onBackground,
        contentColor = TrackerNewTheme.colors.oppositeColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = TrackerNewTheme.colors.tintColor
        )
    }
}

@Composable
fun OutlinedTextFieldCategory(
    state: AddLessonNameStore.State,
    modifier: Modifier = Modifier,
    onClearIconClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        delay(550)
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(modifier),
        value = state.lessonName,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = stringResource(R.string.name_of_lesson),
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
                color = if (state.lessonName.isNotEmpty()) Green200 else Color.Red,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}