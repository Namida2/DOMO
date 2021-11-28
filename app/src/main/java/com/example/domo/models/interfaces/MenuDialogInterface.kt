package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.menu.CategoriesNameHolder
import entities.menu.Category
import entities.menu.Dish

interface MenuDialogInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    val menuCategoriesName: CategoriesNameHolder
    fun getDishById(id: Int): Dish
}