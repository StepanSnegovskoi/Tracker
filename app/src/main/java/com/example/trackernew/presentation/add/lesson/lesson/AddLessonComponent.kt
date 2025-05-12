package com.example.trackernew.presentation.add.lesson.lesson

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddLessonComponent {

    val model: StateFlow<AddLessonStore.State>

    val labels: Flow<AddLessonStore.Label>

    fun onAddLessonClicked()


    fun onLessonNameClickedAndLessonNamesListIsEmpty()

    fun onLecturerClickedAndLecturersListIsEmpty()

    fun onAudienceClickedAndAudiencesListIsEmpty()


    fun onNameChanged(name: String)

    fun onLecturerChanged(lecturer: String)

    fun onAudienceChanged(audience: String)

    fun onStartChanged(start: Long)

    fun onEndChanged(end: Long)

    fun onTypeOfLessonChanged(typeOfLesson: String)
}