package com.example.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.di.CoreDepsStore.deps
import com.example.core.domain.tools.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseActivityViewModel: ViewModel(){
    private val _newPermissionEvent = MutableLiveData<Event<Unit>>()
    open val newPermissionEvent: LiveData<Event<Unit>> = _newPermissionEvent
    protected fun listenPermissionChanges(scope: CoroutineScope) {
        viewModelScope.launch {
            EmployeePermissionListener.permissionChanges.collect {
                if(deps.currentEmployee?.email == it.email && !it.permission)
                    _newPermissionEvent.value = Event(Unit)
            }
        }
    }
}