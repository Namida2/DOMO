package com.example.domo.splashScreen.data

import com.example.domo.splashScreen.domain.MenuService
import com.example.waiter_core.data.database.daos.MenuDao
import com.example.waiter_core.domain.menu.Category
import com.example.waiter_core.domain.menu.Dish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuLocalRepositoryImpl @Inject constructor(
    private val menuService: MenuService,
    private val menuDao: MenuDao
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
                menuService.setMenuServiceState(menu)
            }
        }
    }

    override fun insertCurrentMenu(menuService: MenuService) {
        val dishes = getAllDishes(menuService.menu)
        CoroutineScope(IO).launch {
            menuDao.insert(dishes)
        }
    }

    private fun getAllDishes(menu: ArrayList<Category>): List<Dish> =
        menu.map { it.dishes }.flatten()

}

interface MenuLocalRepository {
    fun readExitingMenu()
    fun insertCurrentMenu(menuService: MenuService)
}

