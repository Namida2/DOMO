package application.interfaces

import androidx.lifecycle.LiveData
import entities.Dish

sealed class MenuHolderStates() {
    object EmptyMenu: MenuHolderStates()
    object Loaded: MenuHolderStates()
    object Loading: MenuHolderStates()
    object Default: MenuHolderStates()
}

interface MenuHolder {
    var menuState: LiveData<MenuHolderStates>
    var menu: List<Dish>
}