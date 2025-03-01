package com.example.trackernew.presentation.add.task

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AddTaskContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column {
            OutlinedTextFieldName()
            OutlinedTextFieldDescription()
            OutlinedTextFieldDeadline()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldName() {
    var expanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {

        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            label = {
                Text(text = "Название")
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        },
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null,

                    )
            },
            value = "",
            onValueChange = {

            }
        )

        ExposedDropdownMenu(
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = {

            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "text")
                },
                onClick = {
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "text")
                },
                onClick = {
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun OutlinedTextFieldDescription() {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Описание")
        },
        value = "",
        onValueChange = {

        }
    )
}

@Composable
fun OutlinedTextFieldDeadline() {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = {
            Text(text = "Дедлайн")
        },
        value = "",
        onValueChange = {

        }
    )
}