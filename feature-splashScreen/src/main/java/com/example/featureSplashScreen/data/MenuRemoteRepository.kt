package com.example.domo.splashScreen.data

import com.example.core.domain.menu.Category
import com.example.core.domain.menu.Dish
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.FirestoreReferences.menuCollectionRef
import com.example.core.domain.tools.extensions.logE
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private val menu: ArrayList<Category> = ArrayList()

    //TODO: Handle the case when there is no internet connection
    override fun readNewMenu(onComplete: () -> Unit) {
        MenuService.setMenuServiceStateAsLoading()
        menuCollectionRef.get().addOnSuccessListener {
            val categoriesLastIndex: Int = it.documents.lastIndex
            it.documents.forEachIndexed { index, documentSnapshot ->
                if (index == categoriesLastIndex)
                    readDishes(documentSnapshot.id, true, onComplete)
                else
                    readDishes(documentSnapshot.id, onComplete = onComplete)
            }
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
            menuCollectionRef.document(category)
                .collection(com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_DISHES)
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
        com.example.core.domain.tools.FirestoreReferences.menuDocumentRef.get()
            .addOnSuccessListener {
                val menuVersion =
                    it.data?.get(com.example.core.domain.tools.constants.FirestoreConstants.FIELD_MENU_VERSION)
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