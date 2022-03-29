package com.example.waiterMain.presentation

import androidx.lifecycle.*
import com.example.core.domain.interfaces.BaseActivityViewModel

class WaiterMainViewModel: BaseActivityViewModel() {
    init { listenPermissionChanges(viewModelScope) }
}
