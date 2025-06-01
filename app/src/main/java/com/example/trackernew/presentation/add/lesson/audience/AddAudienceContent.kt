package com.example.trackernew.presentation.add.lesson.audience

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green200
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AddAudienceContent(component: AddAudienceComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                AddAudienceStore.Label.AudienceSaved -> {
                    snackBarManager.showMessage("Аудитория сохранена")
                }

                AddAudienceStore.Label.AddAudienceClickedAndAudienceIsEmpty -> {
                    snackBarManager.showMessage("Название не должно быть пустым")
                }
            }
        }.launchIn(rememberCoroutineScope)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = TrackerNewTheme.colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    component.onAddAudienceClicked()
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = TrackerNewTheme.colors.linearGradientBackground)
        ) {
            OutlinedTextFieldAudience(
                state = state,
                modifier = Modifier
                    .padding(paddingValues),
                onValueChange = {
                    component.onAudienceChanged(it)
                },
                onClearIconClick = {
                    component.onClearAudienceClicked()
                }
            )
        }
    }
}

@Composable
fun OutlinedTextFieldAudience(
    state: AddAudienceStore.State,
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
        value = state.audience,
        onValueChange = {
            onValueChange(it)
        },
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
                        onClearIconClick()
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TrackerNewTheme.colors.tintColor
            )
        },
        supportingText = {
            Text(
                text = "*Обязательно",
                color = if (state.audience.isNotEmpty()) Green200 else Color.Red,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}