package com.example.trackernew.presentation.add.lesson.audience

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
import com.example.trackernew.ui.theme.Green
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AddAudienceContent(component: AddAudienceComponent, snackbarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when(it){
                AddAudienceStore.Label.AudienceSaved -> {
                    snackbarManager.showMessage("Название сохранено")
                }

                AddAudienceStore.Label.AddAudienceClickedAndNameIsEmpty -> {
                    snackbarManager.showMessage("Название не должно быть пустым")
                }
            }
        }.launchIn(rememberCoroutineScope)
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        containerColor = TrackerNewTheme.colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    component.onAddClicked()
                },
                containerColor = TrackerNewTheme.colors.onBackground,
                contentColor = TrackerNewTheme.colors.oppositeColor
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        OutlinedTextFieldAudience(
            state = state,
            modifier = Modifier
                .padding(paddingValues),
            onValueChange = {
                component.onAudienceChanged(it)
            },
        )
    }
}

@Composable
fun OutlinedTextFieldAudience(
    state: AddAudienceStore.State,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(modifier),
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
        supportingText = {
            Text(
                text = "*Обязательно",
                color = if(state.audience.isNotEmpty()) Green else Color.Red,
                fontSize = 12.sp
            )
        }
    )
}