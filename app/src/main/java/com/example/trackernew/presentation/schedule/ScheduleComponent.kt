package com.example.trackernew.presentation.schedule

import kotlinx.coroutines.flow.StateFlow

interface ScheduleComponent {

    val model: StateFlow<ScheduleStore.State>

    fun onAddWeekClicked()

    fun onEditWeeksClicked()

    fun onAddLessonClicked(weekId: Int, dayName: String, futureLessonId: Int)

    fun onDeleteLessonClicked(weekId: Int, lessonId: Int)
}