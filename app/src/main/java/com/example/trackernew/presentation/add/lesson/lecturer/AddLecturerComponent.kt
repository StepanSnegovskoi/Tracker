package com.example.trackernew.presentation.add.lesson.lecturer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddLecturerComponent {

    val model: StateFlow<AddLecturerStore.State>

    val labels: Flow<AddLecturerStore.Label>

    fun onAddLecturerClicked()

    fun onLecturerChanged(lecturer: String)
}