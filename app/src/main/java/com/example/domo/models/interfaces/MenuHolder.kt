package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.menu.Category
import entities.menu.CategoryName
import entities.menu.Dish

sealed class MenuHolderStates {
    object MenuExist : MenuHolderStates()
    object MenuEmpty : MenuHolderStates()
    object MenuIsLoading : MenuHolderStates()
    object Default : MenuHolderStates()
}

interface MenuLocalRepository {
    fun readNewMenu(onComplete: () -> Unit)
    fun readExitingMenu()
}

interface MenuHolder {
    val menuState: LiveData<MenuHolderStates>
    var menu: ArrayList<Category>
    fun getAllCategories(): List<CategoryName>
    fun getDishById(dishId: Int): Dish?
}