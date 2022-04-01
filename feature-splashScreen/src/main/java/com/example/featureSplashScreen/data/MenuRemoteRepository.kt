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
    private var collectionMenuSize: Int = 0
    private val menu = mutableListOf<Category>()

    //TODO: Handle the case when there is no internet connection
    override fun readNewMenu(onComplete: () -> Unit) {
        MenuService.setMenuServiceStateAsLoading()
        menuCollectionRef.get(Source.SERVER).addOnSuccessListener {
            collectionMenuSize = it.documents.size
            logD("documentsCount: ${it.documents.size}")
            it.documents.forEach { documentSnapshot ->
                readDishes(documentSnapshot.id, onComplete)
            }
        }.addOnFailureListener {
            logE("$this: ${it.message}")
        }
    }

    private fun readDishes(
        category: String,
        onComplete: () -> Unit,
    ) {
        logD("readDishes: $category")
        menuCollectionRef.document(category)
            .collection(FirestoreConstants.COLLECTION_DISHES).get(Source.SERVER).addOnSuccessListener {
                val dishes = mutableListOf<Dish>()
                logD("$category: ${it.documents.size}")
                it.documents.forEach { dish ->
                    dish.toObject(Dish::class.java)?.let { notNulDish ->
                        dishes.add(notNulDish)
                    }
                }
                menu.add(Category(category, dishes))
                if (menu.size == collectionMenuSize) {
                    logD("isItLastCategory: $collectionMenuSize")
                    logD("menuCollectionSize: ${menu.size}")
                    onMenuLoadingFinish(onComplete)
                }
            }.addOnFailureListener {
                logE("$this: ${it.message}")
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
