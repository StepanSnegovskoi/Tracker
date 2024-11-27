package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracker.R
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.useCases.DeleteGroupUseCase
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import com.example.tracker.presentation.fragments.FragmentGroups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentGroupsViewModel @Inject constructor(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase
) : ViewModel() {

    private val _listGroups: MutableLiveData<List<Group>> = MutableLiveData()
    val listGroups: LiveData<List<Group>>
        get() = _listGroups

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadGroups()
        }
    }

    suspend fun deleteGroup(groupName: String){
        deleteGroupUseCase(groupName)
    }

    suspend fun loadGroups() {
        viewModelScope.launch {
            getAllGroupsUseCase().apply {
                withContext(Dispatchers.Main){
                    _listGroups.value = this@apply
                }
            }
        }
    }
}
