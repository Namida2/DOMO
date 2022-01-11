package entities.menu

import com.example.waiter_core.domain.menu.Dish

data class Category(val name: String, val dishes: List<Dish>) {
    fun getDishById(id: Int): Dish? = dishes.find { it.id == id }
}