package com.example.domo.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domo.models.interfaces.MenuHolder
import com.example.domo.models.interfaces.MenuHolderStates
import com.example.domo.views.log
import com.google.firebase.firestore.FirebaseFirestore
import constants.FirestoreConstants
import database.daos.MenuDao
import entities.Category
import entities.Dish
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MenuService @Inject constructor(
    private var menuDao: MenuDao,
    private val fireStore: FirebaseFirestore) : MenuHolder {
    private val menuCollectionRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS).document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_MENU)
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
            log("$this: ${it.message}")
            onMenuLoadingFinish(onComplete)
        }
    }

    private fun readDishes(
        category: String,
        isItLastCategory: Boolean = false,
        onComplete: () -> Unit
    ) {
        val dishesCollectionRef =
            menuCollectionRef.document(category).collection(FirestoreConstants.COLLECTION_DISHES)
        dishesCollectionRef.get().addOnSuccessListener {
            val dishes: ArrayList<Dish> = ArrayList()
            for (document in it)
                dishes.add(document.toObject(Dish::class.java))
            menu.add(Category(category, dishes))
            if(isItLastCategory) onMenuLoadingFinish(onComplete)
        }.addOnFailureListener {
            log("$this: ${it.message}")
            onMenuLoadingFinish(onComplete)
        }
    }

    private fun onMenuLoadingFinish(onComplete: () -> Unit) {
        if(menu.isEmpty()) _menuState.value = MenuHolderStates.MenuEmpty
        else {
            _menuState.value = MenuHolderStates.MenuExist
            CoroutineScope(IO).launch {
                menuDao.insert(getAllDishes())
            }
        }
        onComplete()
    }

    private fun getAllDishes(): List<Dish> {
        val arrayList = ArrayList<Dish>()
        menu.forEach {
            arrayList.addAll(it.dishes)
        }
        return arrayList
    }

    override fun readExitingMenu() {
        CoroutineScope(IO).launch {
            val categories: MutableSet<String> = mutableSetOf()
            val allDishes = menuDao.readAll()
            allDishes.forEach {
                categories.add(it.categoryName)
            }
            categories.forEach { category ->
                var dishes = allDishes.filter { it.categoryName == category }
                menu.add(Category(category, dishes))
            }
        }
    }
}