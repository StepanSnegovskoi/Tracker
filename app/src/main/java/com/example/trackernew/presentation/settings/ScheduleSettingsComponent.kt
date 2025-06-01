package com.example.trackernew.presentation.settings

import kotlinx.coroutines.flow.StateFlow

interface ScheduleSettingsComponent {

    val model: StateFlow<ScheduleSettingsStore.State>

    fun onDeleteLecturerClicked(name: String)

    fun onDeleteLessonNameClicked(name: String)

    fun onDeleteAudienceClicked(name: String)
}