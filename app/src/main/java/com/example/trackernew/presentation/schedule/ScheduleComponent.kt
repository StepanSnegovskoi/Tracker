package com.example.trackernew.presentation.schedule

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ScheduleComponent {

    val model: StateFlow<ScheduleStore.State>

    val labels: Flow<ScheduleStore.Label>

    fun onAddWeekClicked()

    fun onEditWeeksClicked()

    fun onSettingsClicked()

    fun onAddLessonClicked(weekId: Int, dayName: String, futureLessonId: Int)

    fun onAddLessonClickedAndWeeksAreEmpty()

    fun onDeleteLessonClicked(weekId: Int, lessonId: Int)

    fun onNavigateToCurrentDayClickedAndDaysListIsEmpty()
}