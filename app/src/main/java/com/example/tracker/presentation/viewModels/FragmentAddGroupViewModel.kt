package com.example.tracker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tracker.domain.entities.Group
import com.example.tracker.domain.useCases.AddGroupUseCase
import com.example.tracker.domain.useCases.GetAllGroupsUseCase
import com.example.tracker.presentation.sealed.framentAddGroup.State
import com.example.tracker.presentation.sealed.framentAddGroup.Error
import com.example.tracker.presentation.sealed.framentAddGroup.GroupName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentAddGroupViewModel @Inject constructor(
    private val addGroupUseCase: AddGroupUseCase,
    private val getAllGroupsUseCase: GetAllGroupsUseCase
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State>
        get() = _state

    suspend fun addGroup(name: String?) {
        withContext(Dispatchers.IO) {
            name?.trim()?.let { nameTrimmed ->
                when {

                    nameTrimmed.isEmpty() -> {
                        withContext(Dispatchers.Main) {
                            _state.value = Error("Группу с таким названием добавить нельзя")
                        }
                    }

                    getAllGroupsUseCase().contains(Group(nameTrimmed)) -> {
                        withContext(Dispatchers.Main) {
                            _state.value = Error("Группа с таким названием уже существует")
                        }
                    }

                    else -> {
                        addGroupUseCase(Group(nameTrimmed))
                        withContext(Dispatchers.Main) {
                            _state.value = GroupName(nameTrimmed)
                        }
                    }
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    _state.value = Error("Неизвестная ошибка")
                }
            }
        }
    }
}
