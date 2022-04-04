package com.example.core.data.repositorues

import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.Dish
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.constants.FirestoreReferences
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.repositories.MenuRemoteRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private var collectionMenuSize: Int = 0
    private var menu = mutableListOf<Category>()

    override fun readNewMenu(menuCollectionRef: CollectionReference, task: SimpleTask) {
        menu = mutableListOf()
        MenuService.setMenuServiceStateAsLoading()
        menuCollectionRef.get(Source.SERVER).addOnSuccessListener {
            collectionMenuSize = it.documents.size
            logD("documentsCount: ${it.documents.size}")
            it.documents.forEach { documentSnapshot ->
                readDishes(menuCollectionRef, documentSnapshot.id, task)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun readDishes(
        menuCollectionRef: CollectionReference,
        category: String,
        task: SimpleTask
    ) {
        logD("readDishes: $category")
        menuCollectionRef.document(category)
            .collection(FirestoreConstants.COLLECTION_DISHES).get(Source.SERVER)
            .addOnSuccessListener {
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
                    onMenuLoadingFinish(task)
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }

    private fun onMenuLoadingFinish(task: SimpleTask) {
        MenuService.setNewMenu(menu)
        task.onSuccess(Unit)
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
