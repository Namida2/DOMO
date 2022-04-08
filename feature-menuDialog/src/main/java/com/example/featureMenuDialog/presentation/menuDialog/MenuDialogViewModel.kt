package com.example.featureMenuDialog.presentation.menuDialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.menu.CategoryName
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.dto.DeletedDishInfo
import com.example.core.domain.entities.tools.Event
import com.example.core.presentation.recyclerView.interfaces.BaseRecyclerViewType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class NewMenu(
        val items: List<BaseRecyclerViewType>,
    ) : MenuDialogStates()
}

class MenuDialogViewModel : ViewModel() {

    private val wiveTypesBefore = 1
    private var recyclerViewItems: List<BaseRecyclerViewType>? = null
    private var _state: MutableLiveData<MenuDialogStates> = MutableLiveData()
    val state: LiveData<MenuDialogStates> = _state
    private var _onDishSelected: MutableLiveData<Event<Dish>> = MutableLiveData()
    val onDishSelected: LiveData<Event<Dish>> = _onDishSelected

    private val _onDishDeleted = MutableLiveData<Event<DeletedDishInfo>>()
    val onDishDeleted: LiveData<Event<DeletedDishInfo>> = _onDishDeleted

    init {
        viewModelScope.launch {
            MenuService.menuChanges.collect {
                _state.value = MenuDialogStates.NewMenu(getRecyclerViewItems())
            }
        }
    }

    fun onDishDelete(position: Int) {
        val dish = recyclerViewItems?.get(position) as? Dish ?: return
        _onDishDeleted.value = Event(
            MenuService.deleteDish(dish)
        )
    }

    fun addDish(deletedDishInfo: DeletedDishInfo) {
        MenuService.addDish(deletedDishInfo)
    }

    fun onDishClick(dish: Dish) {
        _onDishSelected.value = Event(dish)
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewType> =
        listOf(MenuService.getAllCategories(wiveTypesBefore)) +
                MenuService.menu.map { category ->
                    listOf(
                        CategoryName(category.name)
                    ) + category.dishes
                }.flatten().also {
                    recyclerViewItems = it
                }

}