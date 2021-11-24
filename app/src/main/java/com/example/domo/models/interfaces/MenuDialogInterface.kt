package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.CategoriesHolder
import entities.Category
import entities.CategoryName
import entities.recyclerView.CategoryViewHolder

interface MenuDialogInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
    val menuCategories: CategoriesHolder
}