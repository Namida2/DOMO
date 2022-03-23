package com.example.featureSettings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.menu.Category
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.menu.isTheSameMenu
import com.example.core.domain.tools.Event
import com.example.featureMenuDialog.domain.interfaces.OnDismissListener
import com.example.featureSettings.domain.useCases.SaveMenuUseCase

class SettingsViewModel(
//    private val sameMenuUseCase: SaveMenuUseCase()
) : ViewModel(), OnDismissListener {

    private lateinit var lastSavedMenu: MutableList<Category>
    private val _onMenuDialogDismissEvent = MutableLiveData<Event<Unit>>()
    val onMenuDialogDismissEvent: LiveData<Event<Unit>> = _onMenuDialogDismissEvent

    override fun onDismiss() {
        if(MenuService.menu isTheSameMenu lastSavedMenu) return
        _onMenuDialogDismissEvent.value = Event(Unit)
    }

    fun onAcceptNewMenu() {

    }
    fun onCancelNewMenu() {
        MenuService.menu = lastSavedMenu
    }

    fun sameMenuBeforeChanges() {
        lastSavedMenu = copyMenu()
    }

    private fun copyMenu(): MutableList<Category> =
        MenuService.menu.map{ category ->
            val newDishes = mutableListOf<Dish>().also {
                category.dishes.forEach { dish ->
                    it.add(dish.copy())
                }
            }
            category.copy(dishes = newDishes)
        }.toMutableList()

}