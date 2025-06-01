package com.example.trackernew.presentation.add.week

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddWeekComponent {

    val model: StateFlow<AddWeekStore.State>

    val labels: Flow<AddWeekStore.Label>

    fun onAddWeekClicked()

    fun onClearWeekClicked()


    fun onWeekChanged(week: String)
}