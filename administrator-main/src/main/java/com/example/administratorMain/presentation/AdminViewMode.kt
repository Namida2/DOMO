package com.example.administratorMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.core.domain.BaseActivityViewModel

class AdminViewMode : BaseActivityViewModel() {
    init {
        listenPermissionChanges(viewModelScope)
    }
}
