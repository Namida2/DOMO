package com.example.cookMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.core.domain.BaseActivityViewModel

class CookViewModel : BaseActivityViewModel() {
    init {
        listenPermissionChanges(viewModelScope)
    }
}