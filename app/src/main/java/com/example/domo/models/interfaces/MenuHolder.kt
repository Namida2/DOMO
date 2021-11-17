package com.example.domo.models.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import entities.Dish

sealed class MenuHolderStates {
    object MenuExist: MenuHolderStates()
    object MenuEmpty: MenuHolderStates()
    object MenuIsLoading: MenuHolderStates()
    object Default: MenuHolderStates()
}

interface MenuHolder {
    var menuState: LiveData<MenuHolderStates>
    var menu: List<Dish>
}