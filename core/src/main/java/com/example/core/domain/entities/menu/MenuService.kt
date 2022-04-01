package com.example.core.domain.entities.menu

import com.example.core.domain.entities.tools.DeletedDishInfo
import com.example.core.domain.entities.tools.constants.OtherStringConstants.THIS_DISH_ALREADY_ADDED
import com.example.core.domain.entities.tools.constants.OtherStringConstants.UNKNOWN_DISH_ID
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.interfaces.BaseObservable
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
            menu.forEach {
                it.dishes.forEach { dish ->
                    logD(dish.toString())
                }
            }
            menu.sort()
            this.menu = menu
            menuState = MenuServiceStates.MenuExists(this)
            notifyChanges()
            menuChanges.tryEmit(MenuService.menu)
        }
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
                category = it
                return@also
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

    fun addDish(dish: Dish): Boolean {
        menu.find {
            it.name == dish.categoryName
        }.also { category ->
            if (category == null) return false
            category.dishes.find {
                it.name == dish.name
            }.also { existingDish ->
                if (existingDish != null) return false
                category.dishes.add(dish)
                category.dishes.sort()
                menuChanges.tryEmit(menu)
                return true
            }
        }
    }

    fun addCategory(categoryName: String): Boolean {
        menu.find {
            it.name == categoryName
        }.also { category ->
            if (category != null) return false
            menu.add(Category(categoryName, mutableListOf()))
            menuChanges.tryEmit(menu)
            return true
        }
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

    fun copyMenu(): MutableList<Category> =
        menu.map { category ->
            val newDishes = mutableListOf<Dish>().also {
                category.dishes.forEach { dish ->
                    it.add(dish.copy())
                }
            }
            category.copy(dishes = newDishes)
        }.toMutableList()

    fun getUniqueId(): Int {
        var id = menu.sumOf { it.dishes.size }
        while (
            menu.map { category ->
                category.dishes.find { it.id == id } != null
            }.contains(true)
        ) {
            id++
        }
        return id
    }

    fun resetState() {
        menuState = MenuServiceStates.Default
    }
}

infix fun Collection<Category>.isTheSameMenu(other: Collection<Category>): Boolean {
    if (this.size != other.size) return false
    val areNotEqual = this.asSequence()
        .zip(other.asSequence())
        .map { (fromThis, fromOther) ->
            if (fromThis != fromOther) false
            else fromThis.dishes.zip(fromOther.dishes).map { (dishesFromThis, dishesFromOther) ->
                dishesFromThis == dishesFromOther
            }
        }.contains(false)
    return !areNotEqual
}
