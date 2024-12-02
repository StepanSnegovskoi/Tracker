package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.useCases.DeleteGroupUseCase
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import com.example.tracker.presentation.sealed.fragmentGroups.LoadGroups
import com.example.tracker.presentation.sealed.fragmentGroups.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentGroupsViewModel @Inject constructor(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadGroups()
        }
    }

    fun deleteGroup(groupName: String){
        viewModelScope.launch (Dispatchers.IO) {
            deleteGroupUseCase(groupName)
            loadGroups()
        }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase().apply {
                withContext(Dispatchers.Main){
                    _state.value = LoadGroups(this@apply)
                }
            }
        }
    }
}
