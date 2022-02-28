package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import com.example.core.domain.menu.CategoriesNameHolder
import com.example.core.domain.menu.Category
import com.example.core.domain.menu.Dish

interface MenuDialogModelInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    fun getDishById(id: Int): Dish
    fun getAllCategories(): CategoriesNameHolder
}