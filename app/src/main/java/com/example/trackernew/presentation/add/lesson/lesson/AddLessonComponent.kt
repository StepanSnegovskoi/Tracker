package com.example.trackernew.presentation.add.lesson.lesson

import kotlinx.coroutines.flow.StateFlow

interface AddLessonComponent {

    val model: StateFlow<AddLessonStore.State>

    fun onSaveLessonClicked()

    fun onNameChanged(name: String)
}