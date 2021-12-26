package com.example.domo.viewModels.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.models.interfaces.MenuDialogModelInterface
import com.example.domo.models.interfaces.MenuHolderStates
import entities.menu.CategoryName
import entities.menu.Dish
import entities.recyclerView.interfaces.BaseRecyclerViewItem
import entities.tools.Event

sealed class MenuDialogStates {
    object Default : MenuDialogStates()
    class MenuExists(
        val items: List<BaseRecyclerViewItem>,
    ) : MenuDialogStates()

    object ShowingDishDialog : MenuDialogStates()
}

class MenuDialogViewModel(
    private val model: MenuDialogModelInterface,
) : ViewModel() {
    private var _state: MutableLiveData<MenuDialogStates> =
        MutableLiveData(MenuDialogStates.Default)
    val state: LiveData<MenuDialogStates> = _state

    private var _onDishSelected: MutableLiveData<Event<Dish>> = MutableLiveData()
    val onDishSelected: LiveData<Event<Dish>> = _onDishSelected

    init {
        when (model.menuState.value) {
            is MenuHolderStates.MenuIsLoading ->
                model.menuState.observeForever {
                    when (it) {
                        MenuHolderStates.MenuExist -> {
                            _state.value = MenuDialogStates.MenuExists(getRecyclerViewItems())
                        }
                        else -> { }//Default state
                    }
                }
            is MenuHolderStates.MenuExist -> _state.value =
                MenuDialogStates.MenuExists(getRecyclerViewItems())
            else -> { } //TODO: Somehow remove the Default state
        }
    }

    private fun getRecyclerViewItems(): List<BaseRecyclerViewItem> =
        listOf(model.getAllCategories()) +
                model.menu.map {
                    listOf(CategoryName(it.name)) + it.dishes
                }.flatten()

    fun onDishClick(dishId: Int) {
        _onDishSelected.value = Event(model.getDishById(dishId))
    }
}