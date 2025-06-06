package com.example.trackernew.presentation.add.lesson.audience

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddAudienceComponent {

    val model: StateFlow<AddAudienceStore.State>

    val labels: Flow<AddAudienceStore.Label>

    fun onAddAudienceClicked()

    fun onClearAudienceClicked()


    fun onAudienceChanged(audience: String)
}