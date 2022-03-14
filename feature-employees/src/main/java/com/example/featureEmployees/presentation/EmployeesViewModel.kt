package com.example.featureEmployees.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Employee
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.ErrorMessages.defaultErrorMessage
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.SimpleTask
import com.example.featureEmployees.domain.EmployeesService
import com.example.featureEmployees.domain.ReadEmployeesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class EmployeesVMStates {
    object Default : EmployeesVMStates()
    object ReadingData : EmployeesVMStates()
    object EmptyEmployeesList : EmployeesVMStates()
    object ReadingWasSuccessful : EmployeesVMStates()
    class ReadingFailed(val errorMessage: ErrorMessage) : EmployeesVMStates()
}

typealias NewEmployeesEvent = Event<List<Employee>>

class EmployeesViewModel(
    private val readEmployeesUseCase: ReadEmployeesUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<EmployeesVMStates>()
    val state: LiveData<EmployeesVMStates> = _state

    private val _newEmployeesEvent = MutableLiveData<NewEmployeesEvent>()
    val newEmployeesEvent: LiveData<NewEmployeesEvent> = _newEmployeesEvent

    init {
        viewModelScope.launch {
            EmployeesService.employeesChanges.collect {
                _newEmployeesEvent.value = Event(it.toList())
            }
        }
    }

    fun readEmployees() {
        _state.value = EmployeesVMStates.ReadingData
        readEmployeesUseCase.readEmployees(object : SimpleTask {
            override fun onSuccess(arg: Unit) {
                _state.value = EmployeesVMStates.ReadingWasSuccessful
            }

            override fun onError(message: ErrorMessage?) {
                _state.value = EmployeesVMStates.ReadingFailed(message ?: defaultErrorMessage)
            }
        })

    }
}