package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.menu.CategoriesNameHolder
import entities.menu.Category
import entities.menu.CategoryName
import entities.menu.Dish

interface MenuDialogInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    fun getDishById(id: Int): Dish
    fun getAllCategories(): CategoriesNameHolder
}