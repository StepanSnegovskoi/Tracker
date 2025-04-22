package com.example.trackernew.presentation.add.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.sp
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors

@Composable
fun AddCategoryContent(component: AddCategoryComponent) {
    val state by component.model.collectAsState()
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
        OutlinedTextFieldCategory(
            state = state,
            modifier = Modifier
                .padding(paddingValues),
            onValueChange = {
                component.onCategoryChanged(it)
            },
        )
    }
}

@Composable
fun OutlinedTextFieldCategory(
    state: AddCategoryStore.State,
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
                text = "Категория",
                color = TrackerNewTheme.colors.textColor
            )
        },
        colors = getOutlinedTextFieldColors(),
        value = state.category,
        onValueChange = {
            onValueChange(it)
        },
        supportingText = {
            Text(
                text = "*Обязательно",
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    )
}