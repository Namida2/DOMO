package com.example.domo.models

import com.example.domo.models.interfaces.MenuDialogInterface
import com.example.domo.models.interfaces.MenuHolder
import entities.menu.CategoriesNameHolder
import entities.menu.Dish
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MenuDialogModel @Inject constructor(
    private val menuHolder: MenuHolder,
) : MenuDialogInterface {
    override val menu = menuHolder.menu
    override val menuState = menuHolder.menuState
    override val menuCategoriesName = CategoriesNameHolder(menuHolder.getAllCategories())
    override fun getDishById(id: Int): Dish {
        var dish: Dish? = null
        menu.first {
            dish = it.getDishById(id)
            dish != null
        }
        return dish ?: throw IllegalArgumentException("Dish not found. id: $id")
    }
}