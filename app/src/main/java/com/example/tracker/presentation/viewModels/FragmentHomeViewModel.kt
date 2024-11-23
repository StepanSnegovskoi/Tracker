package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.useCases.GetCardsByGroupNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentHomeViewModel @Inject constructor(
    private val getCardsByGroupNameUseCase: GetCardsByGroupNameUseCase
) : ViewModel() {

    private val _listCards: MutableLiveData<List<Card>> = MutableLiveData()
    val listCards: LiveData<List<Card>>
        get() = _listCards

    suspend fun getCardsByName(groupName: String) {
        withContext(Dispatchers.IO) {
            getCardsByGroupNameUseCase(groupName).let {
                withContext(Dispatchers.Main) {
                    _listCards.value = it
                }
            }
        }
    }
}
