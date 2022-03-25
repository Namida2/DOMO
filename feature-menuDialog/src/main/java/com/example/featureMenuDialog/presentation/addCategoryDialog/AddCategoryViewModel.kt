package com.example.featureMenuDialog.presentation.addCategoryDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.interfaces.TerminatingState
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.ErrorMessage
import com.example.core.domain.tools.constants.ErrorMessages

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
        else setNewState(AddCategoryVMStates.CategoryAlreadyExists(ErrorMessages.dishAlreadyExists))
    }

    override fun setNewState(state: AddCategoryVMStates) {
        _state.value = AddCategoryVMStates.Default
        if (state !is TerminatingState) return
        _state.value = AddCategoryVMStates.Default
    }
}