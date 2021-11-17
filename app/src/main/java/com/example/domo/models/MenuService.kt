package com.example.domo.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.MenuHolderStates
import entities.Dish
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuService @Inject constructor() : MenuHolder {
    override var menuState: LiveData<MenuHolderStates> = MutableLiveData(MenuHolderStates.Default)
    override var menu: List<Dish> = emptyList()
}