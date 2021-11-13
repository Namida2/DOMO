package com.example.domo.models

import entities.Dish
import extentions.interfaces.BaseObservable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

typealias MenuServiceObserver = (menu: ArrayList<Dish>) -> Unit
typealias MenuServiceObservable = BaseObservable<@JvmSuppressWildcards MenuServiceObserver>

//TODO: Add this into appGraph
//TODO: Add interfaces for repositories
@Singleton
class MenuService @Inject constructor() : MenuServiceObservable {
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