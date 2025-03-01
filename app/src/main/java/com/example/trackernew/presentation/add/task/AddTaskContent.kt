package com.example.trackernew.presentation.add.task

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

val items = listOf("Математика", "КГ", "Диффуры")

@Preview
@Composable
fun AddTaskContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            OutlinedTextFieldNameWithMenu()
            OutlinedTextFieldDescriptionWithMenu()
            OutlinedTextFieldDeadlineWithMenu()
        }
    }
}

@Composable
fun OutlinedTextFieldNameWithMenu() {
    val expanded = remember {
        mutableStateOf(false)
    }
    Menu(
        expanded = expanded,
        items = items,
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldName(
                modifier = modifier,
                onIconClick = {
                    expanded.value = !expanded.value
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldName(
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        readOnly = true,
        label = {
            Text(text = "Название")
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onIconClick()
                    },
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
            )
        },
        value = "",
        onValueChange = {
        }
    )
}

@Composable
fun OutlinedTextFieldDescriptionWithMenu() {
    val expanded = remember {
        mutableStateOf(false)
    }
    Menu(
        expanded = expanded,
        items = items,
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldDescription (
                modifier = modifier,
                onIconClick = {
                    expanded.value = !expanded.value
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldDescription(
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        readOnly = true,
        label = {
            Text(text = "Описание")
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onIconClick()
                    },
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
            )
        },
        value = "",
        onValueChange = {
        }
    )
}

@Composable
fun OutlinedTextFieldDeadlineWithMenu() {
    val expanded = remember {
        mutableStateOf(false)
    }
    Menu(
        expanded = expanded,
        items = items,
        onDismissRequest = {
            expanded.value = false
        },
        onItemClick = {
            expanded.value = false
        },
        content = { modifier ->
            OutlinedTextFieldDeadline (
                modifier = modifier,
                onIconClick = {
                    expanded.value = !expanded.value
                }
            )
        }
    )
}

@Composable
fun OutlinedTextFieldDeadline(
    modifier: Modifier = Modifier,
    onIconClick: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        readOnly = true,
        label = {
            Text(text = "Дедлайн")
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .clickable {
                        onIconClick()
                    },
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
            )
        },
        value = "",
        onValueChange = {
        }
    )
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