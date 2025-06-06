package com.example.trackernew.presentation.add.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AddCategoryComponent {

    val model: StateFlow<AddCategoryStore.State>

    val labels: Flow<AddCategoryStore.Label>

    fun onAddCategoryClicked()

    fun onClearCategoryClicked()


    fun onCategoryChanged(category: String)
}