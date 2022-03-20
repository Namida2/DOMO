package com.example.core.domain.menu

data class Category(val name: String, val dishes: MutableList<Dish>): Comparable<Category> {
    fun getDishById(id: Int): Dish? = dishes.find { it.id == id }
    override fun compareTo(other: Category): Int = name.compareTo(other.name)
}