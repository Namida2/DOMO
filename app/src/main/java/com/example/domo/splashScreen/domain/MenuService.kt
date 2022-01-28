package com.example.domo.splashScreen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.waiterCore.domain.menu.Category
import com.example.waiterCore.domain.menu.CategoryName
import com.example.waiterCore.domain.menu.Dish
import javax.inject.Inject
import javax.inject.Singleton


sealed class MenuServiceStates {
    class MenuExists(
        val menuService: MenuService,
    ) : MenuServiceStates()
    object MenuIsEmpty : MenuServiceStates()
    object MenuIsLoading : MenuServiceStates()
    object Default : MenuServiceStates()
}

@Singleton
class MenuService @Inject constructor() {

    var menu: ArrayList<Category> = ArrayList()

    private val _menuState: MutableLiveData<MenuServiceStates> =
        MutableLiveData(MenuServiceStates.Default)
    val menuState: LiveData<MenuServiceStates> = _menuState

    fun getAllCategories(): List<CategoryName> =
        menu.map {
            it.name
        }.map {
            CategoryName(it)
        }

    fun getDishById(dishId: Int): Dish? {
        var dish: Dish? = null
        menu.find { category ->
            dish = category.dishes.find {
                it.id == dishId
            }
            dish != null
        }
        return dish
    }

    fun setMenuServiceState(menu: ArrayList<Category>?) {
        if (menu.isNullOrEmpty()) _menuState.value = MenuServiceStates.MenuIsEmpty
        else {
            this.menu = menu
            _menuState.value = MenuServiceStates.MenuExists(this)
        }
    }
    fun setMenuServiceStateAsLoading() {
        _menuState.value = MenuServiceStates.MenuIsLoading
    }

}