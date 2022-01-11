package com.example.domo.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.waiter_core.domain.tools.constants.FirestoreConstants
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.MenuHolderStates
import com.example.domo.models.interfaces.MenuLocalRepository
import com.example.waiter_core.domain.tools.FirestoreReferences.menuCollectionRef
import com.example.waiter_core.data.database.daos.MenuDao
import entities.menu.Category
import entities.menu.CategoryName
import com.example.waiter_core.domain.menu.Dish
import com.example.waiter_core.domain.tools.extentions.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuService @Inject constructor(
    private var menuDao: MenuDao,
) : MenuHolder, MenuLocalRepository {

    private val _menuState: MutableLiveData<MenuHolderStates> =
        MutableLiveData(MenuHolderStates.Default)
    override val menuState: LiveData<MenuHolderStates> = _menuState
    override var menu: ArrayList<Category> = ArrayList()

    override fun readNewMenu(onComplete: () -> Unit) {
        _menuState.value = MenuHolderStates.MenuIsLoading
        menuCollectionRef.get().addOnSuccessListener {
            val categoriesCount: Int = it.size()
            for (i in 0 until categoriesCount)
                if (i == categoriesCount - 1)
                    readDishes(it.documents[i].id, true, onComplete)
                else
                    readDishes(it.documents[i].id, onComplete = onComplete)
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            onMenuLoadingFinish(onComplete)
        }
    }

    private fun readDishes(
        category: String,
        isItLastCategory: Boolean = false,
        onComplete: () -> Unit,
    ) {
        val dishesCollectionRef =
            menuCollectionRef.document(category).collection(FirestoreConstants.COLLECTION_DISHES)
        dishesCollectionRef.get().addOnSuccessListener {
            val dishes: ArrayList<Dish> = ArrayList()
            for (document in it)
                dishes.add(document.toObject(Dish::class.java))
            menu.add(Category(category, dishes))
            if (isItLastCategory) onMenuLoadingFinish(onComplete)
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            onMenuLoadingFinish(onComplete)
        }
    }

    private fun onMenuLoadingFinish(onComplete: () -> Unit) {
        CoroutineScope(IO).launch {
            menuDao.insert(getAllDishes())
        }
        setMenuServiceState()
        onComplete()
    }

    private fun setMenuServiceState() {
        if (menu.isEmpty()) _menuState.value = MenuHolderStates.MenuEmpty
        else _menuState.value = MenuHolderStates.MenuExist
    }

    private fun getAllDishes(): List<Dish> =
        menu.map { it.dishes }.flatten()

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
            withContext(Main) {
                setMenuServiceState()
            }
        }
    }

    override fun getAllCategories(): List<CategoryName> =
        menu.map {
            it.name
        }.map {
            CategoryName(it)
        }

    override fun getDishById(dishId: Int): Dish? {
        var dish: Dish? = null
        menu.find { category ->
            dish = category.dishes.find {
                it.id == dishId
            }
            dish != null
        }
        return dish
    }

}