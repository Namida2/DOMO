package com.example.featureMenuDialog.presentation.addCategoryDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.constants.Messages

sealed class AddCategoryVMStates {
    class CategoryAlreadyExists(val message: ErrorMessage) : AddCategoryVMStates(), TerminatingState
    object CategoryAdded : AddCategoryVMStates(), TerminatingState
    object Default : AddCategoryVMStates()
}

class AddCategoryViewModel : ViewModel(), Stateful<AddCategoryVMStates> {

    private val _state = MutableLiveData<AddCategoryVMStates>()
    val state: LiveData<AddCategoryVMStates> = _state

    fun addCategory(categoryName: String) {
        if (MenuService.addCategory(categoryName))
            setNewState(AddCategoryVMStates.CategoryAdded)
        else setNewState(AddCategoryVMStates.CategoryAlreadyExists(Messages.categoryAlreadyExists))
    }

    override fun setNewState(state: AddCategoryVMStates) {
        _state.value = state
        if (state !is TerminatingState) return
        _state.value = AddCategoryVMStates.Default
    }
}