package com.example.trackernew.presentation.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.trackernew.presentation.add.category.AddCategoryContent
import com.example.trackernew.presentation.add.task.AddTaskContent
import com.example.trackernew.presentation.edit.EditTaskContent
import com.example.trackernew.presentation.tasks.TasksContent
import com.example.trackernew.ui.theme.TrackerNewTheme

@Composable
fun RootContent(component: RootComponent) {
    TrackerNewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Children(stack = component.stack) {
                when (val instance = it.instance) {

                    is RootComponent.Child.AddTask -> {
                        AddTaskContent(instance.component)
                    }
                    is RootComponent.Child.Tasks -> {
                        TasksContent(instance.component)
                    }
                    is RootComponent.Child.EditTask -> {
                        EditTaskContent(instance.component)
                    }
                    is RootComponent.Child.AddCategory -> {
                        AddCategoryContent(instance.component)
                    }
                }
            }
        }
    }

}