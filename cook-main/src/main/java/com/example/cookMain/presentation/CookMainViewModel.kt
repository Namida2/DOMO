package com.example.cookMain.presentation

import androidx.lifecycle.viewModelScope
import com.example.cookMain.domain.di.CookMainDepsStore
import com.example.core.domain.interfaces.BaseActivityViewModel
import com.example.core.domain.listeners.MenuVersionListener

class CookMainViewModel(
    menuVersionListener: MenuVersionListener
) : BaseActivityViewModel(menuVersionListener) {

    init {
        listenPermissionChanges(viewModelScope)
    }

    override fun onCleared() {
        CookMainDepsStore.onCleared()
        super.onCleared()
    }
}