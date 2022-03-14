package com.example.featureOrder.presentation.order.doalogs.menuDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.menu.CategoryName
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.menu.MenuServiceStates
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val types: List<BaseRecyclerViewType>,
    ) : MenuDialogStates()

    object ShowingDishDialog : MenuDialogStates()
}

class MenuDialogViewModel(
    private val menuService: MenuService,
) : ViewModel() {
    private var _state: MutableLiveData<MenuDialogStates> =
        MutableLiveData(MenuDialogStates.Default)
    val state: LiveData<MenuDialogStates> = _state

    private var _onDishSelected: MutableLiveData<com.example.core.domain.tools.Event<Dish>> =
        MutableLiveData()
    val onDishSelected: LiveData<com.example.core.domain.tools.Event<Dish>> = _onDishSelected

    init {
        when (menuService.menuState) {
            is MenuServiceStates.MenuIsLoading ->
                menuService.subscribe {
                    when (it) {
                        is MenuServiceStates.MenuExists -> {
                            _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
                        }
                        else -> {}//Default state
                    }
                }
            is MenuServiceStates.MenuExists -> _state.value =
                MenuDialogStates.MenuExists(getRecyclerViewItems())
            else -> {}
        }
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewType> =
        listOf(menuService.getAllCategories()) +
                menuService.menu.map {
                    listOf(CategoryName(it.name)) + it.dishes
                }.flatten()

    fun onDishClick(dishId: Int) {
        _onDishSelected.value = com.example.core.domain.tools.Event(menuService.getDishById(dishId))
    }
}