package com.example.waiter_core.domain.menu

data class Category(val name: String, val dishes: List<Dish>) {
    fun getDishById(id: Int): Dish? = dishes.find { it.id == id }
}