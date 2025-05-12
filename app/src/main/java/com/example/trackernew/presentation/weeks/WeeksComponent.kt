package com.example.trackernew.presentation.weeks

import com.example.trackernew.domain.entity.Week
import kotlinx.coroutines.flow.StateFlow

interface WeeksComponent {

    val model: StateFlow<WeeksStore.State>


    fun onSelectWeekAsCurrentClicked(week: Week)

    fun onConfirmEditClicked()

    fun onDeleteWeekClicked(weekId: Int)

    fun onMoveUpWeekClicked(week: Week)

    fun onMoveDownWeekClicked(week: Week)


    fun onWeekStatusChanged(week: Week)
}