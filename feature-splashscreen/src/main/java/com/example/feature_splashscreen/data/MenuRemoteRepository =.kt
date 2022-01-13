package com.example.feature_splashscreen.data

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.feature_splashscreen.domain.MenuService
import com.example.waiter_core.data.database.daos.MenuDao
import com.example.waiter_core.domain.menu.Category
import com.example.waiter_core.domain.menu.Dish
import com.example.waiter_core.domain.tools.FirestoreReferences
import com.example.waiter_core.domain.tools.constants.FirestoreConstants
import com.example.waiter_core.domain.tools.extentions.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed class MenuHolderStates {
    class MenuExist(
        val menuService: MenuService,
    ) : MenuHolderStates()
    object MenuEmpty : MenuHolderStates()
    object MenuIsLoading : MenuHolderStates()
    object Default : MenuHolderStates()
}

//TODO: Implement an observable pattern // STOPPED_0 //
class MenuRemoteRepositoryImpl @Inject constructor(
    override var menuService: MenuService,
    private var menuDao: MenuDao,
    private var sharedPreferences: SharedPreferences,
) : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private val _menuState: MutableLiveData<MenuHolderStates> =
        MutableLiveData(MenuHolderStates.Default)
    val menuState: LiveData<MenuHolderStates> = _menuState
    private val menu: ArrayList<Category> = ArrayList()

    override fun readNewMenu(onComplete: () -> Unit) {
        _menuState.value = MenuHolderStates.MenuIsLoading
        FirestoreReferences.menuCollectionRef.get().addOnSuccessListener {
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
            FirestoreReferences.menuCollectionRef.document(category)
                .collection(FirestoreConstants.COLLECTION_DISHES)
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
        CoroutineScope(Dispatchers.IO).launch {
            menuDao.insert(getAllDishes())
        }
        setMenuServiceState()
        onComplete()
    }


    private fun getAllDishes(): List<Dish> =
        menu.map { it.dishes }.flatten()

    override fun readExitingMenu() {
        CoroutineScope(Dispatchers.IO).launch {
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
                setMenuServiceState()
            }
        }
    }

    override fun readMenuVersion(onComplete: (version: Long) -> Unit) {
        FirestoreReferences.menuDocumentRef.get().addOnSuccessListener {
            val menuVersion = it.data?.get(FirestoreConstants.FIELD_MENU_VERSION)
            if (menuVersion != null) {
                onComplete(menuVersion as Long)
            } else {
                logE("$this: MenuVersion in the remote data source is null")
                onComplete(defaultMenuVersion)
            }
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            onComplete(defaultMenuVersion)
        }
    }

    private fun setMenuServiceState() {
        if (menu.isEmpty()) _menuState.value = MenuHolderStates.MenuEmpty
        else {
            menuService.setMenu(menu)
            _menuState.value = MenuHolderStates.MenuExist(menuService)
        }
    }

}

interface MenuRemoteRepository {
    var menuService: MenuService
    fun readNewMenu(onComplete: () -> Unit)
    fun readExitingMenu()
    fun readMenuVersion(onComplete: (version: Long) -> Unit)
}