package com.example.trackernew.presentation.root

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.trackernew.presentation.add.category.AddCategoryContent
import com.example.trackernew.presentation.add.task.AddTaskContent
import com.example.trackernew.presentation.edit.EditTaskContent
import com.example.trackernew.presentation.tasks.TasksContent
import com.example.trackernew.ui.theme.TrackerNewTheme

private const val TIME_IN_MILLIS_FOR_ANIMATED_CHANGE_CONTENT = 500

@Composable
fun RootContent(component: RootComponent) {
    TrackerNewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Children(
                stack = component.stack,
                animation = stackAnimation(
                    slide(
                        animationSpec = tween(
                            durationMillis =
                            TIME_IN_MILLIS_FOR_ANIMATED_CHANGE_CONTENT
                        )
                    )
                )
            ) {
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