package com.example.trackernew.presentation.root

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.trackernew.presentation.add.category.AddCategoryContent
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceComponent
import com.example.trackernew.presentation.add.lesson.audience.AddAudienceContent
import com.example.trackernew.presentation.add.lesson.lecturer.AddLecturerContent
import com.example.trackernew.presentation.add.lesson.lesson.AddLessonContent
import com.example.trackernew.presentation.add.lesson.name.AddLessonNameContent
import com.example.trackernew.presentation.add.task.AddTaskContent
import com.example.trackernew.presentation.add.week.AddWeekContent
import com.example.trackernew.presentation.edit.EditTaskContent
import com.example.trackernew.presentation.schedule.ScheduleContent
import com.example.trackernew.presentation.tasks.TasksContent
import com.example.trackernew.ui.theme.TrackerNewTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

private const val TIME_IN_MILLIS_FOR_ANIMATED_CHANGE_CONTENT = 500
private const val FAB_BOTTOM_PADDING_INT = 72
private const val ENABLE_EDGE_TO_EDGE_BOTTOM_PADDING = 12

@Composable
fun RootContent(component: RootComponent, snackbarManager: SnackbarManager) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarManager) {
        snackbarManager.messages.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

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
                        AddTaskContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.Tasks -> {
                        TasksContent(instance.component)
                    }

                    is RootComponent.Child.EditTask -> {
                        EditTaskContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.AddCategory -> {
                        AddCategoryContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.AddLesson -> {
                        AddLessonContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.AddLessonName -> {
                        AddLessonNameContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.AddLecturer -> {
                        AddLecturerContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.AddAudience -> {
                        AddAudienceContent(instance.component, snackbarManager)
                    }

                    is RootComponent.Child.Schedule -> {
                        ScheduleContent(instance.component)
                    }

                    is RootComponent.Child.AddWeek -> {
                        AddWeekContent(instance.component, snackbarManager)
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = (
                                FAB_BOTTOM_PADDING_INT + ENABLE_EDGE_TO_EDGE_BOTTOM_PADDING).dp
                    )
            )
        }
    }
}

class SnackbarManager @Inject constructor() {
    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}