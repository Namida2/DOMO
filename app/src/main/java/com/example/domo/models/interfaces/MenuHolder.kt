package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import entities.Category
import entities.CategoryName
import entities.Dish
import entities.Task

sealed class MenuHolderStates {
    object MenuExist: MenuHolderStates()
    object MenuEmpty: MenuHolderStates()
    object MenuIsLoading: MenuHolderStates()
    object Default: MenuHolderStates()
}

interface MenuLocalRepository {
    fun readNewMenu(onComplete: () -> Unit)
    fun readExitingMenu()
}

interface MenuHolder {
    val menuState: LiveData<MenuHolderStates>
    var menu: ArrayList<Category>
    fun getAllCategories(): List<CategoryName>
}