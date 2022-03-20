package com.example.core.domain.menu

import com.example.core.domain.interfaces.BaseObservable
import com.example.core.domain.tools.DeletedDishInfo
import com.example.core.domain.tools.constants.OtherStringConstants.THIS_DISH_ALREADY_ADDED
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
            menu.map { it.name }.map {
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

    fun setNewMenu(menu: MutableList<Category>?) {
        if (menu.isNullOrEmpty()) menuState = MenuServiceStates.MenuIsEmpty
        else {
            menu.sort()
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
        menu.find { category ->
            category.dishes.indexOfFirst {
                it.id == dish.id
            }.let { index ->
                if (index == -1) return@find false
                category.dishes.removeAt(index)
                deletedDishInfo = DeletedDishInfo(category.name, dish)
                return@find true
            }
        }.also {
            it ?: return@also
            if (it.dishes.isEmpty()) menu.remove(it)
        }
        return if (deletedDishInfo != null) {
            menuChanges.tryEmit(menu)
            deletedDishInfo!!
        } else throw IllegalArgumentException(UNKNOWN_DISH_ID + dish.id)
    }

    fun addDish(deletedDishInfo: DeletedDishInfo) {
        var category: Category
        menu.find {
            it.name == deletedDishInfo.categoryName
        }.also {
            if (it != null) {
                category = it; return@also
            }
            menu.add(
                Category(
                    deletedDishInfo.categoryName,
                    mutableListOf(deletedDishInfo.dish)
                )
            ).also {
                menu.sort()
                menuChanges.tryEmit(menu)
                return
            }
        }

        category.dishes.find {
            it.id == deletedDishInfo.dish.id
        }.also { dish ->
            //TODO: Remove this and return false
            if (dish != null) throw IllegalArgumentException(THIS_DISH_ALREADY_ADDED + dish.id)
        }

        category.dishes.add(deletedDishInfo.dish).also {
            category.dishes.sort()
        }
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
