package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.useCases.AddCardUseCase
import com.example.tracker.domain.useCases.DeleteCardAndReturnItUseCase
import com.example.tracker.domain.useCases.GetCardUseCase
import com.example.tracker.domain.useCases.GetCardsByGroupNameUseCase
import com.example.tracker.presentation.sealed.fragmentHome.LoadCards
import com.example.tracker.presentation.sealed.fragmentHome.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentHomeViewModel @Inject constructor(
    private val getCardsByGroupNameUseCase: GetCardsByGroupNameUseCase,
    private val deleteCardAndReturnItUseCase: DeleteCardAndReturnItUseCase,
    private val addCardUseCase: AddCardUseCase,
    private val getCardUseCase: GetCardUseCase,
) : ViewModel() {

    private val deletedCards = mutableListOf<Card>()

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    fun deleteCardById(id: Int) {
        viewModelScope.launch (Dispatchers.IO) {
            val groupName = getCardGroupNameById(id)
            deleteCardAndReturnItUseCase(id).apply {
                getCardsByName(groupName)
                deletedCards.add(this)
            }
        }
    }

    fun returnCards(groupName: String) {
        if(deletedCards.size > 0) {
            viewModelScope.launch (Dispatchers.IO) {
                deletedCards.forEach {
                    addCardUseCase(it)
                }
                deletedCards.clear()
                getCardsByName(groupName)
            }
        }
    }

    fun loadCards(groupName: String){
        getCardsByName(groupName)
    }

    private fun getCardsByName(groupName: String) {
        viewModelScope.launch (Dispatchers.IO){
            getCardsByGroupNameUseCase(groupName).apply {
                withContext(Dispatchers.Main) {
                    _state.value = LoadCards(this@apply)
                }
            }
        }
    }

    private suspend fun getCardGroupNameById(id: Int): String {
        return viewModelScope.async (Dispatchers.IO) {
            getCardUseCase(id).groupName
        }.await()
    }
}
