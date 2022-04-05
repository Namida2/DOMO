package com.example.featureSettings.data.remositories

import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.COLLECTION_DISHES
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_MENU_VERSION
import com.example.core.domain.entities.tools.constants.FirestoreReferences.actualMenuCollectionRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.fireStore
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuDocumentRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.entities.tools.constants.Messages.ordersCollectionNotEmptyMessage
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.core.domain.entities.tools.extensions.logD
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {
    private var currentCopiedMenu: MutableList<Category> = mutableListOf()
    private var initialSize = 0
    private var remoteCollectionSize = 0
    private var deletedCategoriesCount = 0
    private var insertedCategoriesCount = 0
    private var targetMenuCollection: CollectionReference = actualMenuCollectionRef

    override fun saveNewMenu(targetMenuCollection: CollectionReference, task: SimpleTask) {
        this.targetMenuCollection = targetMenuCollection
        prepareForUpdating()
        logD("insertedCategoriesCount: $insertedCategoriesCount")
        logD("initialSize: $initialSize")
        if (targetMenuCollection == actualMenuCollectionRef)
            checkOrders(task) { deleteOldMenu(task) }
        else deleteOldMenu(task)
    }

    private fun prepareForUpdating() {
        var id = 0
        initialSize = 0
        deletedCategoriesCount = 0
        insertedCategoriesCount = 0
        remoteCollectionSize = 0
        currentCopiedMenu = MenuService.copyMenu()
        val iterator = currentCopiedMenu.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.dishes.isEmpty()) iterator.remove()
            next.dishes.forEach { dish ->
                dish.id = id++
            }
        }
        initialSize = currentCopiedMenu.size
    }

    private fun checkOrders(task: SimpleTask, onSuccess: () -> Unit) {
        ordersCollectionRef.get(Source.SERVER).addOnSuccessListener {
            if (it.documents.isNotEmpty()) {
                task.onError(ordersCollectionNotEmptyMessage)
                return@addOnSuccessListener
            }
            onSuccess()
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun deleteOldMenu(task: SimpleTask) {
        targetMenuCollection.get(Source.SERVER).addOnSuccessListener {
            remoteCollectionSize = it.documents.size
            if (remoteCollectionSize == 0) insertNewMenu(task)
            it.documents.forEach { category ->
                deleteAllDishes(category.id, task)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun deleteAllDishes(
        categoryName: String,
        task: SimpleTask,
    ) {
        targetMenuCollection.document(categoryName).collection(COLLECTION_DISHES).get()
            .addOnSuccessListener {
                logD(categoryName)
                val lastIndex = it.documents.lastIndex
                if (lastIndex == -1) {
                    ++deletedCategoriesCount
                    onCategoryDeleted(categoryName, task)
                    if (deletedCategoriesCount == remoteCollectionSize)
                        insertNewMenu(task)
                }
                it.documents.forEachIndexed { index, dish ->
                    targetMenuCollection.document(categoryName).collection(COLLECTION_DISHES)
                        .document(dish.id).delete().addOnSuccessListener {
                            logD("deleted: $dish")
                            if (index == lastIndex) {
                                ++deletedCategoriesCount
                                onCategoryDeleted(categoryName, task)
                            }
                            if (index == lastIndex && deletedCategoriesCount == remoteCollectionSize)
                                insertNewMenu(task)
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }

    private fun onCategoryDeleted(categoryName: String, task: SimpleTask) {
        val category = currentCopiedMenu.find {
            it.name == categoryName
        } ?: targetMenuCollection.document(categoryName).delete().also { return }
        currentCopiedMenu.remove(category as Category)
        insertDishes(category, task)
    }

    private fun insertNewMenu(task: SimpleTask) {
        val lastIndexOfCategory = currentCopiedMenu.lastIndex
        logD("menuSize in insertNewMenu: ${currentCopiedMenu.size}")
        if (lastIndexOfCategory == -1) return
        currentCopiedMenu.forEach { category ->
            insertDishes(category, task)
        }
    }

    private fun insertDishes(
        category: Category,
        task: SimpleTask,
    ) {
        logD("lastIndexOfDish in ${category.name}")
        targetMenuCollection.document(category.name)
            .set(mapOf(category.name to category.dishes.size))
            .addOnSuccessListener {
                val lastIndex = category.dishes.lastIndex
                category.dishes.forEachIndexed { index, dish ->
                    targetMenuCollection.document(category.name).collection(COLLECTION_DISHES)
                        .document(dish.name).set(dish).addOnSuccessListener {
                            logD("set ${dish.name}")
                            logD("insertedCategoriesCount $insertedCategoriesCount")
                            if (lastIndex == index) ++insertedCategoriesCount
                            if (lastIndex == index && insertedCategoriesCount == initialSize) {
                                logD("insertDishes::onSuccess")
                                setNewMenuVersion(task)
                            }
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }

    private fun setNewMenuVersion(task: SimpleTask) {
        if (targetMenuCollection != actualMenuCollectionRef) {
            task.onSuccess(Unit)
            return
        }
        deleteOrdersCollection(task) {
            fireStore.runTransaction {
                logD("____setNewMenuVersion____")
                var menuVersion = it.get(menuDocumentRef).get(FIELD_MENU_VERSION) as Long
                it.update(menuDocumentRef, FIELD_MENU_VERSION, ++menuVersion)
            }.addOnSuccessListenerWithDefaultFailureHandler(task) {
                task.onSuccess(Unit)
            }
        }
    }

    private fun deleteOrdersCollection(task: SimpleTask, onComplete: () -> Unit) {
        ordersCollectionRef.get(Source.SERVER).addOnSuccessListenerWithDefaultFailureHandler(task) {
            val lastIndex = it.result.documents.lastIndex
            logD("deleteOrdersCollection: $lastIndex")
            if (lastIndex == -1) onComplete()
            it.result.forEachIndexed { index, orderId ->
                ordersCollectionRef.document(orderId.id).delete()
                    .addOnSuccessListenerWithDefaultFailureHandler(task) {
                        if (index == lastIndex) onComplete()
                    }
            }
        }
    }
}