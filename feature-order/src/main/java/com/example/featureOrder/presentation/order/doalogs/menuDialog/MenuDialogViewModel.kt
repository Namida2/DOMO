package com.example.featureOrder.presentation.order.doalogs.menuDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.waiterCore.domain.menu.CategoryName
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.menu.MenuServiceStates
import com.example.waiterCore.domain.recyclerView.interfaces.BaseRecyclerViewItem
import com.example.waiterCore.domain.tools.Event

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val items: List<BaseRecyclerViewItem>,
    ) : MenuDialogStates()
    object ShowingDishDialog : MenuDialogStates()
}

class MenuDialogViewModel(
    private val menuService: MenuService,
) : ViewModel() {
    private var _state: MutableLiveData<MenuDialogStates> =
        MutableLiveData(MenuDialogStates.Default)
    val state: LiveData<MenuDialogStates> = _state

    private var _onDishSelected: MutableLiveData<Event<Dish>> = MutableLiveData()
    val onDishSelected: LiveData<Event<Dish>> = _onDishSelected

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

    private fun getRecyclerViewItems(): List<BaseRecyclerViewItem> =
        listOf(menuService.getAllCategories()) +
                menuService.menu.map {
                    listOf(CategoryName(it.name)) + it.dishes
                }.flatten()

    fun onDishClick(dishId: Int) {
        _onDishSelected.value = Event(menuService.getDishById(dishId))
    }
}