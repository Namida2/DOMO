package com.example.core.domain.menu

import com.example.core.domain.interfaces.BaseObservable
import com.example.core.domain.tools.DeletedDishInfo
import com.example.core.domain.tools.constants.OtherStringConstants.THIS_DISH_ALREADY_ADDED
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_CATEGORY_NAME
import com.example.core.domain.tools.constants.OtherStringConstants.UNKNOWN_DISH_ID
import kotlinx.coroutines.flow.MutableSharedFlow

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

    var menu: MutableList<Category> = mutableListOf()
    var menuState: MenuServiceStates = MenuServiceStates.Default
    private var subscribers: MutableSet<MenuServiceSub> = mutableSetOf()
    val menuChanges = MutableSharedFlow<List<Category>>(replay = 1)

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
        menuChanges.tryEmit(MenuService.menu)
    }

    fun setMenuServiceStateAsLoading() {
        menuState = MenuServiceStates.MenuIsLoading
    }

    fun deleteDish(dish: Dish): DeletedDishInfo {
        var deletedDishInfo: DeletedDishInfo? = null
        menu.forEach { category ->
            category.dishes.indexOfFirst {
                it.id == dish.id
            }.also { index ->
                if (index == -1) return@also
                category.dishes.removeAt(index)
                deletedDishInfo = DeletedDishInfo(category.name, dish)
                return@forEach
            }
        }
        return if (deletedDishInfo != null) {
            menuChanges.tryEmit(menu)
            deletedDishInfo!!
        } else throw IllegalArgumentException(UNKNOWN_DISH_ID + dish.id)
    }

    fun addDish(deletedDishInfo: DeletedDishInfo) {
        val category = menu.find {
            it.name == deletedDishInfo.categoryName
        } ?: throw IllegalArgumentException(UNKNOWN_CATEGORY_NAME + deletedDishInfo.categoryName)

        category.dishes.find {
            it.id == deletedDishInfo.dish.id
        }.also { dish ->
            if (dish != null) throw IllegalArgumentException(THIS_DISH_ALREADY_ADDED + dish.id)
        }

        category.dishes.add(deletedDishInfo.dish)
        menuChanges.tryEmit(menu)
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