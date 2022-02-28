package com.example.domo.splashScreen.data

import com.example.core.domain.menu.Category
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuLocalRepositoryImpl @Inject constructor(
    private val menuDao: com.example.core.data.database.daos.MenuDao
) : MenuLocalRepository {

    private var menu: ArrayList<Category> = ArrayList()

    override fun readExitingMenu() {
        CoroutineScope(IO).launch {
            val categories: MutableSet<String> = mutableSetOf()
            val allDishes = menuDao.readAll()
            allDishes.forEach {
                categories.add(it.categoryName)
            }
            categories.forEach { category ->
                val dishes = allDishes.filter { it.categoryName == category }
                menu.add(Category(category, dishes))
            }
            withContext(Dispatchers.Main) {
                MenuService.setMenuServiceState(menu)
            }
        }
    }

    override fun insertCurrentMenu() {
        val dishes = getAllDishes(MenuService.menu)
        CoroutineScope(IO).launch {
            menuDao.insert(dishes)
        }
    }

    private fun getAllDishes(menu: ArrayList<Category>): List<Dish> =
        menu.map { it.dishes }.flatten()

}

interface MenuLocalRepository {
    fun readExitingMenu()
    fun insertCurrentMenu()
}

