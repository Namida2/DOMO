package com.example.core.domain.menu

data class Category(val name: String, val dishes: MutableList<Dish>) {
    fun getDishById(id: Int): Dish? = dishes.find { it.id == id }
}