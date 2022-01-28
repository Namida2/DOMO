package com.example.domo.models.dialogs

import com.example.domo.models.interfaces.MenuDialogModelInterface
import com.example.domo.models.interfaces.MenuHolder
import com.example.waiterCore.domain.menu.CategoriesNameHolder
import com.example.waiterCore.domain.menu.Dish
import javax.inject.Inject

class MenuDialogModel @Inject constructor(
    private val menuHolder: MenuHolder,
) : MenuDialogModelInterface {
    override val menu = menuHolder.menu
    override val menuState = menuHolder.menuState
    override fun getAllCategories(): CategoriesNameHolder =
        CategoriesNameHolder(menuHolder.getAllCategories())
    override fun getDishById(id: Int): Dish {
        var dish: Dish? = null
        menu.first {
            dish = it.getDishById(id)
            dish != null
        }
        return dish ?: throw IllegalArgumentException("Dish not found. id: $id")
    }
}