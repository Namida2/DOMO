package com.example.featureMenuDialog.presentation.editMenuItemDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.constants.ErrorMessages.categoryAlreadyExists
import com.example.featureMenuDialog.domain.tools.MenuItemsEnum

sealed class EditMenuItemVMState {
    class ItemAlreadyExists(val message: ErrorMessage) : EditMenuItemVMState()
    object ItemAdded : EditMenuItemVMState()
    object Default : EditMenuItemVMState()
}

class EditMenuItemViewModel : ViewModel() {

    private val _state = MutableLiveData<EditMenuItemVMState>()
    val state: LiveData<EditMenuItemVMState> = _state

    fun addCategory(categoryName: String) {
        if (MenuService.addCategory(categoryName))
            _state.value = EditMenuItemVMState.ItemAdded
        else _state.value = EditMenuItemVMState.ItemAlreadyExists(categoryAlreadyExists)
    }

    fun addDish(
        categoryName: String, name: String,
        cost: String, weight: String
    ) {
        if (MenuService.addDish(
                categoryName,
                Dish(
                    MenuService.getDishesCount(),
                    name, categoryName,
                    cost, weight
                )
            )
        ) _state.value = EditMenuItemVMState.ItemAdded
        else _state.value = EditMenuItemVMState.ItemAlreadyExists(categoryAlreadyExists)
    }

    fun resetState() {
        _state.value = EditMenuItemVMState.Default
    }
}