package com.example.domo.viewModels.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.menu.CategoryName
import com.example.core.domain.menu.Dish
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import com.example.domo.models.interfaces.MenuDialogModelInterface
import com.example.domo.models.interfaces.MenuHolderStates

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val types: List<BaseRecyclerViewType>,
    ) : MenuDialogStates()

    object ShowingDishDialog : MenuDialogStates()
}

class MenuDialogViewModel(
    private val model: MenuDialogModelInterface,
) : ViewModel() {
    private var _state: MutableLiveData<MenuDialogStates> =
        MutableLiveData(MenuDialogStates.Default)
    val state: LiveData<MenuDialogStates> = _state

    private var _onDishSelected: MutableLiveData<com.example.core.domain.tools.Event<Dish>> =
        MutableLiveData()
    val onDishSelected: LiveData<com.example.core.domain.tools.Event<Dish>> = _onDishSelected

    init {
        when (model.menuState.value) {
            is MenuHolderStates.MenuIsLoading ->
                model.menuState.observeForever {
                    when (it) {
                        MenuHolderStates.MenuExist -> {
                            _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
                        }
                        else -> {}//Default state
                    }
                }
            is MenuHolderStates.MenuExist -> _state.value =
                MenuDialogStates.MenuExists(getRecyclerViewItems())
            else -> {}
        }
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewType> =
        listOf(model.getAllCategories()) +
                model.menu.map {
                    listOf(CategoryName(it.name)) + it.dishes
                }.flatten()

    fun onDishClick(dishId: Int) {
        _onDishSelected.value = com.example.core.domain.tools.Event(model.getDishById(dishId))
    }
}