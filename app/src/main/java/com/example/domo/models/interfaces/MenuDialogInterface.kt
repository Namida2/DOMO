package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import entities.Category

interface MenuDialogInterface {
    val menuState: LiveData<MenuHolderStates>
    val menu: List<Category>
}