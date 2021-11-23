package com.example.domo.models

import androidx.lifecycle.LiveData
import com.example.domo.models.interfaces.MenuDialogInterface
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.MenuHolderStates
import entities.Category
import javax.inject.Inject

class MenuDialogModel@Inject constructor(
    private val menuHolder: MenuHolder
): MenuDialogInterface {
    override val menu = menuHolder.menu
    override val menuState = menuHolder.menuState
}