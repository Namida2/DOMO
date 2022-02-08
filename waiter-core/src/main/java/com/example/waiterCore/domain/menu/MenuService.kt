package com.example.waiterCore.domain.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.waiterCore.domain.interfaces.BaseObservable

typealias MenuServiceSub = (state: MenuServiceStates) -> Unit

sealed class MenuServiceStates {
    class MenuExists(
        val menuService: MenuService,
    ) : MenuServiceStates()
    object MenuIsEmpty : MenuServiceStates()
    object MenuIsLoading : MenuServiceStates()
    object Default : MenuServiceStates()
}

object MenuService: BaseObservable<MenuServiceSub> {
    var menu: ArrayList<Category> = ArrayList()

    private var menuState: MenuServiceStates = MenuServiceStates.Default

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
        if (menu.isNullOrEmpty()) menuState = MenuServiceStates.MenuIsEmpty
        else {
            this.menu = menu
            menuState = MenuServiceStates.MenuExists(this)
        }
    }
    fun setMenuServiceStateAsLoading() {
        menuState = MenuServiceStates.MenuIsLoading
    }

    override fun subscribe(subscriber: MenuServiceSub) {
        TODO("Not yet implemented")
    }

    override fun unSubscribe(subscriber: MenuServiceSub) {
        TODO("Not yet implemented")
    }

    override fun notifyChanges() {
        TODO("Not yet implemented")
    }

}