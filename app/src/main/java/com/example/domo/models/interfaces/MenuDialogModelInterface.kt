package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.menu.CategoriesNameHolder
import entities.menu.Category
import com.example.waiter_core.domain.menu.Dish

interface MenuDialogModelInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    fun getDishById(id: Int): Dish
    fun getAllCategories(): CategoriesNameHolder
}