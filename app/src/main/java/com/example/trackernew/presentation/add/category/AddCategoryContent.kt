package com.example.trackernew.presentation.add.category

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.trackernew.R
import com.example.trackernew.presentation.root.SnackbarManager
import com.example.trackernew.ui.theme.Green200
import com.example.trackernew.ui.theme.Red300
import com.example.trackernew.ui.theme.TrackerNewTheme
import com.example.trackernew.ui.theme.getOutlinedTextFieldColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AddCategoryContent(component: AddCategoryComponent, snackBarManager: SnackbarManager) {
    val state by component.model.collectAsState()
    val rememberCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(
        key1 = component
    ) {
        component.labels.onEach {
            when (it) {
                AddCategoryStore.Label.AddCategoryClickedAndCategoryIsEmpty -> {
                    snackBarManager.showMessage(context.getString(R.string.title_should_not_be_blank))
                }

                AddCategoryStore.Label.CategorySaved -> {
                    snackBarManager.showMessage(context.getString(R.string.category_saved))
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
                    component.onCategoryChanged(it)
                },
                onClearIconClick = {
                    component.onClearCategoryClicked()
                }
            )
        }
    }
}

@Composable
private fun FAB(component: AddCategoryComponent) {
    FloatingActionButton(
        modifier = Modifier.imePadding(),
        onClick = {
            component.onAddCategoryClicked()
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
    state: AddCategoryStore.State,
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
        value = state.category,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = "Категория",
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
                color = if (state.category.isNotEmpty()) Green200 else Red300,
                fontSize = 12.sp
            )
        },
        colors = getOutlinedTextFieldColors()
    )
}