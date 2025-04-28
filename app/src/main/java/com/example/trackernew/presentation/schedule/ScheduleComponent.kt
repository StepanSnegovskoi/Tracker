package com.example.trackernew.presentation.schedule

import kotlinx.coroutines.flow.StateFlow

interface ScheduleComponent {

    val model: StateFlow<ScheduleStore.State>

    fun onAddWeekButtonClick()

    fun onAddLessonButtonClick()
}