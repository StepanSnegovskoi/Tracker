package com.example.trackernew.presentation.add.task

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun AddTaskContent() {
    Scaffold (
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
            OutlinedTextFieldDeadline {

            }
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
        readOnly = true,
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
        readOnly = true,
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
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        readOnly = true,
        label = {
            Text(text = "Дедлайн")
        },
        value = "",
        onValueChange = {
            onValueChange(it)
        }
    )
}