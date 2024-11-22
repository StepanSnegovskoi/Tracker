package com.example.tracker.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tracker.domain.useCases.AddCardUseCase
import com.example.tracker.domain.useCases.AddGroupUseCase
import com.example.tracker.domain.useCases.DeleteCardsByGroupNameUseCase
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import com.example.tracker.domain.useCases.GetCardUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val deleteCardsByGroupNameUseCase: DeleteCardsByGroupNameUseCase,
    private val addGroupUseCase: AddGroupUseCase,
    private val getGroupUseCase: GetAllGroupsUseCase
) : ViewModel() {

    suspend fun log() {
        Log.d("MainViewModelLog", "MainViewModelLog")
    }
}
