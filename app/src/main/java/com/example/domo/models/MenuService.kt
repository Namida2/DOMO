package com.example.domo.models

import entities.Dish
import extentions.interfaces.BaseObservable
import java.util.*
import kotlin.collections.ArrayList

typealias MenuServiceObserver = (menu: ArrayList<Dish>) -> Unit

//TODO: Add this into appGraph
//TODO: Add interfaces for repositories
class MenuService: BaseObservable<MenuServiceObserver> {
    private val menu = arrayListOf<Dish>()
    private var observers = mutableSetOf<MenuServiceObserver>()

    override fun addObserver(observer: MenuServiceObserver) {
        observers.add(observer)
    }

    override fun deleteObserver(observer: MenuServiceObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.onEach {
            it.invoke(menu)
        }
    }


}