package com.example.feature_splashscreen.domain

import com.example.waiter_core.domain.menu.Category
import com.example.waiter_core.domain.menu.CategoryName
import com.example.waiter_core.domain.menu.Dish
import javax.inject.Inject


class MenuService @Inject constructor() {

    private var menu: ArrayList<Category> = ArrayList()

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

    fun setMenu(menu: ArrayList<Category> ) {
        this.menu = menu
    }

}