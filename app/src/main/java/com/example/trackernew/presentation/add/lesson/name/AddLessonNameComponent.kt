package com.example.trackernew.presentation.add.lesson.name

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddLessonNameComponent {

    val model: StateFlow<AddLessonNameStore.State>

    val labels: Flow<AddLessonNameStore.Label>

    fun onAddClicked()

    fun onLessonNameChanged(lessonName: String)
}