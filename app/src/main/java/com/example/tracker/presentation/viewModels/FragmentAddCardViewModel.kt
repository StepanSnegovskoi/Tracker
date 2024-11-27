package com.example.tracker.presentation.viewModels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Card
import com.example.tracker.domain.useCases.AddCardUseCase
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import com.example.tracker.presentation.sealed.fragmentAddCard.AddCard
import com.example.tracker.presentation.sealed.fragmentAddCard.Error
import com.example.tracker.presentation.sealed.fragmentAddCard.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentAddCardViewModel @Inject constructor(
    private val addCardUseCase: AddCardUseCase,
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    suspend fun addCard(
        name: String,
        description: String,
        deadline: String,
        groupName: String,
    ) {
        val result = viewModelScope.async {
            withContext(Dispatchers.IO) {
                checkEnteredParams(name, description, deadline, groupName)
            }
        }.await()


        when (result) {

            true -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addCardUseCase(
                        Card(
                            name = name,
                            description = description,
                            deadline = deadline,
                            groupName = groupName,
                        )
                    )
                    withContext(Dispatchers.Main) {
                        _state.value = AddCard
                    }
                }
            }

            false -> {
                _state.value = Error("Название некорректно или группа не существует")
            }
        }
    }

    private suspend fun checkEnteredParams(
        name: String,
        description: String,
        deadline: String,
        groupName: String,
    ): Boolean {
        if (name.isBlank() or groupName.isBlank()) return false
        return getAllGroupsUseCase().map { it.name }.contains(groupName)
    }
}
