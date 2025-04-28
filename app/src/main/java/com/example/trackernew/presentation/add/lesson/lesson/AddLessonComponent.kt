package com.example.trackernew.presentation.add.lesson.lesson

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddLessonComponent {

    val model: StateFlow<AddLessonStore.State>

    val labels: Flow<AddLessonStore.Label>

    fun onSaveLessonClicked()

    fun goToAddNameLessonContent()

    fun onNameChanged(name: String)

    fun goToAddLecturerContent()

    fun onLecturerChanged(lecturer: String)

    fun goToAddAudienceContent()

    fun onAudienceChanged(audience: String)
}