package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentGroupsViewModel @Inject constructor(
    private val getAllGroupsUseCase: GetAllGroupsUseCase
) : ViewModel() {

    private val _listGroups: MutableLiveData<List<Group>> = MutableLiveData()
    val listGroups: LiveData<List<Group>>
        get() = _listGroups

    init {
        viewModelScope.launch (Dispatchers.Main) {
            _listGroups.value = initFirstList()
        }
    }

    private suspend fun initFirstList(): List<Group> {
        return getAllGroupsUseCase()
    }
}
