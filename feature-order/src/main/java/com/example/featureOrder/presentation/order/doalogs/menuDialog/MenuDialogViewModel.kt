package com.example.featureOrder.presentation.order.doalogs.menuDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.menu.CategoryName
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.DeletedDishInfo
import com.example.core.domain.tools.Event
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val items: List<BaseRecyclerViewType>,
    ) : MenuDialogStates()
}

class MenuDialogViewModel(
    private val menuService: MenuService,
) : ViewModel() {

    private var recyclerViewItems: List<BaseRecyclerViewType>? = null
    private var _state: MutableLiveData<MenuDialogStates> = MutableLiveData()
    val state: LiveData<MenuDialogStates> = _state
    private var _onDishSelected: MutableLiveData<Event<Dish>> = MutableLiveData()
    val onDishSelected: LiveData<Event<Dish>> = _onDishSelected

    private val _onDishDeleted = MutableLiveData<Event<DeletedDishInfo>>()
    val onDishDeleted: LiveData<Event<DeletedDishInfo>> = _onDishDeleted

    init {
        viewModelScope.launch {
            menuService.menuChanges.collect {
                _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
            }
        }
    }

    fun onDishDelete(position: Int) {
        val dish = recyclerViewItems?.get(position) as? Dish ?: return
        _onDishDeleted.value = Event(
            menuService.deleteDish(dish)
        )
    }

    fun addDish(deletedDishInfo: DeletedDishInfo) {
        menuService.addDish(deletedDishInfo)
    }

    fun onDishClick(dishId: Int) {
        _onDishSelected.value = Event(menuService.getDishById(dishId))
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewType> =
        listOf(menuService.getAllCategories()) +
                menuService.menu.map {
                    listOf(CategoryName(it.name)) + it.dishes
                }.flatten().also {
                    recyclerViewItems = it
                }
}