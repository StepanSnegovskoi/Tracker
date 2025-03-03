package com.example.trackernew.presentation.add.category

import kotlinx.coroutines.flow.StateFlow

interface AddCategoryComponent {

    val model: StateFlow<AddCategoryStore.State>

    fun onAddClicked()

    fun onCategoryChanged(category: String)
}