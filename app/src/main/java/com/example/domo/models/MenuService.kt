package com.example.domo.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import application.interfaces.MenuHolder
import application.interfaces.MenuHolderStates
import entities.Dish
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuService @Inject constructor(): MenuHolder {
    override var menuState: LiveData<MenuHolderStates> = MutableLiveData(MenuHolderStates.Default)
    override var menu: List<Dish> = emptyList()
}