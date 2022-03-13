package com.example.core.domain.menu

import com.example.core.domain.interfaces.BaseObservable
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_DISH_ID

typealias MenuServiceSub = (state: MenuServiceStates) -> Unit

sealed class MenuServiceStates {
    class MenuExists(
        val menuService: MenuService,
    ) : MenuServiceStates()

    object MenuIsEmpty : MenuServiceStates()
    object MenuIsLoading : MenuServiceStates()
    object Default : MenuServiceStates()
}
//TODO: Create an interface for MenuService to domain layer
object MenuService : BaseObservable<MenuServiceSub> {
    var menu: ArrayList<Category> = ArrayList()

    var menuState: MenuServiceStates = MenuServiceStates.Default
    private var subscribers: MutableSet<MenuServiceSub> = mutableSetOf()

    fun getAllCategories(): CategoriesNameHolder =
        CategoriesNameHolder(
            menu.map {
                it.name
            }.map {
                CategoryName(it)
            }
        )

    fun getDishById(dishId: Int): Dish {
        var dish: Dish? = null
        menu.find { category ->
            dish = category.dishes.find {
                it.id == dishId
            }
            dish != null
        }
        //TODO: Read the menu again in this place if there is an exception
        return dish ?: throw IllegalArgumentException(UNKNOWN_DISH_ID + dishId)
    }

    fun setMenuServiceState(menu: ArrayList<Category>?) {
        if (menu.isNullOrEmpty()) menuState = MenuServiceStates.MenuIsEmpty
        else {
            this.menu = menu
            menuState = MenuServiceStates.MenuExists(this)
        }
        notifyChanges()
    }

    fun setMenuServiceStateAsLoading() {
        menuState = MenuServiceStates.MenuIsLoading
    }

    override fun subscribe(subscriber: MenuServiceSub) {
        subscribers.find {
            it::class.java == subscriber::class.java
        } ?: subscribers.add(subscriber)
        subscriber.invoke(menuState)
    }

    override fun unSubscribe(subscriber: MenuServiceSub) {
        subscribers.remove(subscriber)
    }

    override fun notifyChanges() {
        subscribers.forEach {
            it.invoke(menuState)
        }
    }
}