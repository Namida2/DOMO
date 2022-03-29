package com.example.featureSplashScreen.data

import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuCollectionRef
import com.example.core.domain.entities.tools.extensions.logE
import com.example.featureSplashScreen.domain.repositories.MenuRemoteRepository
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
        MenuService.setNewMenu(menu)
        onComplete()
    }

    override fun readMenuVersion(onComplete: (version: Long) -> Unit) {
        FirestoreReferences.menuDocumentRef.get()
            .addOnSuccessListener {
                val menuVersion =
                    it.data?.get(FirestoreConstants.FIELD_MENU_VERSION)
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
