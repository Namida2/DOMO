package com.example.domo.splashScreen.data

import com.example.waiterCore.domain.menu.MenuService
import com.example.waiterCore.domain.menu.Category
import com.example.waiterCore.domain.menu.Dish
import com.example.waiterCore.domain.tools.FirestoreReferences
import com.example.waiterCore.domain.tools.constants.FirestoreConstants
import com.example.waiterCore.domain.tools.extensions.logE
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private val menu: ArrayList<Category> = ArrayList()

    //TODO: Handle the case when there is no internet connection
    override fun readNewMenu(onComplete: () -> Unit) {
        MenuService.setMenuServiceStateAsLoading()
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
        MenuService.setMenuServiceState(menu)
        onComplete()
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
}

interface MenuRemoteRepository {
    fun readNewMenu(onComplete: () -> Unit)
    fun readMenuVersion(onComplete: (version: Long) -> Unit)
}