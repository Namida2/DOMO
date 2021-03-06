package com.example.core.data.repositorues

import com.example.core.data.database.daos.MenuDao
import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.repositories.MenuLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MenuLocalRepositoryImpl @Inject constructor(
    private val menuDao: MenuDao
) : MenuLocalRepository {

    private var menu = mutableListOf<Category>()

    override fun readExitingMenu() {
        CoroutineScope(IO).launch {
            val categories = mutableSetOf<String>()
            val allDishes = menuDao.readAll()
            logD("readExitingMenu, dishesCount: ${allDishes.size}")
            allDishes.forEach {
                categories.add(it.categoryName)
            }
            categories.forEach { category ->
                val dishes = allDishes.filter { it.categoryName == category } as MutableList
                menu.add(Category(category, dishes))
            }
            withContext(Dispatchers.Main) {
                MenuService.setNewMenu(menu)
            }
        }
    }

    override fun insertCurrentMenu(onComplete: () -> Unit) {
        logD(MenuService.menu.toString())
        val dishes = getAllDishes(MenuService.menu)
        logD("flattenDishesSize: ${dishes.size}")
        CoroutineScope(IO).launch {
            menuDao.insertWithDeleteAldMenu(dishes)
        }
    }

    private fun getAllDishes(menu: MutableList<Category>): List<Dish> =
        menu.map { it.dishes }.flatten()

}

