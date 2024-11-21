package com.example.tracker.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.usecases.AddCardUseCase
import com.example.tracker.domain.usecases.AddGroupUseCase
import com.example.tracker.domain.usecases.DeleteCardsByGroupNameUseCase
import com.example.tracker.domain.usecases.GetAllGroupsUseCase
import com.example.tracker.domain.usecases.GetCardUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val deleteCardsByGroupNameUseCase: DeleteCardsByGroupNameUseCase,
    private val addGroupUseCase: AddGroupUseCase,
    private val getGroupUseCase: GetAllGroupsUseCase
) : ViewModel() {

    suspend fun log() {
        Log.d("MainViewModelLog", "работай")
    }
}
