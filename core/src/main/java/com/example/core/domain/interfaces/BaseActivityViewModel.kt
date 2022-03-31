package com.example.core.domain.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.di.CoreDepsStore.deps
import com.example.core.domain.entities.tools.Event
import com.example.core.domain.listeners.MenuVersionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseActivityViewModel(
    private val menuVersionListener: MenuVersionListener
) : ViewModel() {
    private val _newPermissionEvent = MutableLiveData<Event<Unit>>()
    open val newPermissionEvent: LiveData<Event<Unit>> = _newPermissionEvent
    private val _newMenuVersionEvent = MutableLiveData<Event<Long>>()
    open val newMenuVersionEvent: LiveData<Event<Long>> = _newMenuVersionEvent
    protected fun listenPermissionChanges(scope: CoroutineScope) {
        scope.launch {
            EmployeePermissionListener.permissionChanges.collect {
                if (deps.currentEmployee?.email == it.email && !it.permission)
                    _newPermissionEvent.value = Event(Unit)
            }
        }
    }

    protected fun listenMenuVersionChanges(scope: CoroutineScope) {
        scope.launch {
            menuVersionListener.menuVersionChanges.collect {
                _newMenuVersionEvent.value = Event(it)
            }
        }
    }
}