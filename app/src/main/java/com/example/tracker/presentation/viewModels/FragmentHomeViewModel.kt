package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.useCases.AddCardUseCase
import com.example.tracker.domain.useCases.DeleteCardUseCase
import com.example.tracker.domain.useCases.GetCardsByGroupNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentHomeViewModel @Inject constructor(
    private val getCardsByGroupNameUseCase: GetCardsByGroupNameUseCase,
    private val deleteCardUseCase: DeleteCardUseCase,
    private val addCardUseCase: AddCardUseCase,
) : ViewModel() {

    private val _listDeleted: MutableLiveData<List<Card>> = MutableLiveData()
    val listDeleted: LiveData<List<Card>>
        get() = _listDeleted

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

    suspend fun deleteCardById(id: Int) {
        withContext(Dispatchers.IO) {
            deleteCardUseCase(id).let {
                withContext(Dispatchers.Main) {
                    _listDeleted.value = listOf(it)
                }
            }
        }
    }

    suspend fun returnCard(card: Card) {
        withContext(Dispatchers.IO) {
            addCardUseCase(card)
        }
    }
}
