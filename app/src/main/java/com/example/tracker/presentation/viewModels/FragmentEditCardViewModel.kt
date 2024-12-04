package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.useCases.EditCardUseCase
import com.example.tracker.presentation.sealed.fragmentEditCard.ShouldClose
import com.example.tracker.presentation.sealed.fragmentEditCard.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentEditCardViewModel @Inject constructor(
    private val editCardUseCase: EditCardUseCase
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun editCard(card: Card) {
        viewModelScope.launch {
            editCardUseCase(card)
            withContext(Dispatchers.Main) {
                _state.value = ShouldClose
            }
        }
    }
}