package com.example.waiterMain.presentation

import androidx.lifecycle.*
import com.example.core.data.listeners.EmployeePermissionListener
import com.example.core.domain.BaseActivityViewModel
import com.example.core.domain.tools.Event
import com.example.waiterMain.domain.di.WaiterMainDepsStore.deps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WaiterMainViewModel: BaseActivityViewModel() {
    init { listenPermissionChanges(viewModelScope) }
}
