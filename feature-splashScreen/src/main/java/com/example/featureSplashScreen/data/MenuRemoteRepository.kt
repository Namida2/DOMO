package com.example.featureSplashScreen.data

import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuCollectionRef
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.featureSplashScreen.domain.repositories.MenuRemoteRepository
import com.google.firebase.firestore.Source
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private val menu: ArrayList<Category> = ArrayList()

    //TODO: Handle the case when there is no internet connection
    override fun readNewMenu(onComplete: () -> Unit) {
        MenuService.setMenuServiceStateAsLoading()
        menuCollectionRef.get(Source.SERVER).addOnSuccessListener {
            val categoriesLastIndex = it.documents.lastIndex
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
        logD("readDishes: $category")
        menuCollectionRef.document(category)
            .collection(FirestoreConstants.COLLECTION_DISHES).get().addOnSuccessListener {
                val dishes: ArrayList<Dish> = ArrayList()
                it.documents.forEach { dish ->
                    dish.toObject(Dish::class.java)?.let { notNulDish ->
                        dishes.add(notNulDish)
                        logD("readDishes: $notNulDish")
                    }
                }
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
