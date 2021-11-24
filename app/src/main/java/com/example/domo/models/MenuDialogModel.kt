package com.example.domo.models

import com.example.domo.models.interfaces.MenuDialogInterface
import com.example.domo.models.interfaces.MenuHolder
import entities.CategoriesHolder
import javax.inject.Inject

class MenuDialogModel @Inject constructor(
    private val menuHolder: MenuHolder,
) : MenuDialogInterface {
    override val menu = menuHolder.menu
    override val menuState = menuHolder.menuState
    override val menuCategories = CategoriesHolder(menuHolder.getAllCategories())
}