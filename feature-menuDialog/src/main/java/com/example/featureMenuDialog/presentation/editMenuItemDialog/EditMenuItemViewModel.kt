package com.example.featureMenuDialog.presentation.editMenuItemDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.Messages.dishAlreadyExists
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState

sealed class EditMenuItemVMState {
    class ItemAlreadyExists(val message: ErrorMessage) : EditMenuItemVMState(), TerminatingState
    object ItemAdded : EditMenuItemVMState(), TerminatingState
    object Default : EditMenuItemVMState()
}

class EditMenuItemViewModel : ViewModel(), Stateful<EditMenuItemVMState> {

    private val _state = MutableLiveData<EditMenuItemVMState>()
    val state: LiveData<EditMenuItemVMState> = _state

    fun addDish(
        categoryName: String, dishName: String,
        dishCost: String, dishWeight: String
    ) {
        val dish = Dish(MenuService.getUniqueId(), dishName, categoryName, dishCost, dishWeight)
        if (MenuService.addDish(dish)) setNewState(EditMenuItemVMState.ItemAdded)
        else setNewState(EditMenuItemVMState.ItemAlreadyExists(dishAlreadyExists))
    }

    fun confirmDishChanges(dish: Dish) {
        if (MenuService.confirmDishChanges(dish)) setNewState(EditMenuItemVMState.ItemAdded)
        else setNewState(EditMenuItemVMState.ItemAlreadyExists(dishAlreadyExists))
    }



    override fun setNewState(state: EditMenuItemVMState) {
        _state.value = state
        if (state !is TerminatingState) return
        _state.value = EditMenuItemVMState.Default
    }
}