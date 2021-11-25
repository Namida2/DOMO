package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.menu.CategoriesHolder
import entities.menu.Category

interface MenuDialogInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    val menuCategories: CategoriesHolder
}