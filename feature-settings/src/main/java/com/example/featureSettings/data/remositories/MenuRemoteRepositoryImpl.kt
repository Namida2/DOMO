package com.example.featureSettings.data.remositories

import com.example.core.domain.entities.menu.Category
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants.COLLECTION_DISHES
import com.example.core.domain.entities.tools.constants.FirestoreConstants.FIELD_MENU_VERSION
import com.example.core.domain.entities.tools.constants.FirestoreReferences.fireStore
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuCollectionRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.menuDocumentRef
import com.example.core.domain.entities.tools.constants.FirestoreReferences.ordersCollectionRef
import com.example.core.domain.entities.tools.constants.Messages.ordersCollectionNotEmptyMessage
import com.example.core.domain.entities.tools.extensions.addOnSuccessListenerWithDefaultFailureHandler
import com.example.core.domain.entities.tools.extensions.getExceptionMessage
import com.example.core.domain.entities.tools.extensions.logD
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import javax.inject.Inject
import kotlin.properties.Delegates

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private var currentCopiedMenu: MutableList<Category> = mutableListOf()
    private var initialSize = 0
    private var remoteCollectionSize by Delegates.notNull<Int>()
    private var deletedCategoriesCount = 0
    private var insertedCategoriesCount = 0

    override fun saveNewMenu(task: SimpleTask) {
        initialSize = 0
        deletedCategoriesCount = 0
        insertedCategoriesCount = 0
        currentCopiedMenu =  MenuService.copyMenu()
        initialSize = currentCopiedMenu.size
        logD("insertedCategoriesCount: $insertedCategoriesCount")
        logD("initialSize: $initialSize")
        checkOrders(task) {
            deleteOldMenu(task)
        }
    }

    private fun checkOrders(task: SimpleTask, onSuccess: () -> Unit) {
        ordersCollectionRef.get().addOnSuccessListener {
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
        menuCollectionRef.get().addOnSuccessListener {
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
        menuCollectionRef.document(categoryName).collection(COLLECTION_DISHES).get()
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
                    menuCollectionRef.document(categoryName).collection(COLLECTION_DISHES)
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
            it.name == categoryName // FIXME: Гёдза == Гёдза  
        } ?: menuCollectionRef.document(categoryName).delete().also { return }
        currentCopiedMenu.remove(category as Category)
        insertDishes(category, task)
    }

    private fun insertNewMenu(task: SimpleTask) {
        val lastIndexOfCategory = currentCopiedMenu.lastIndex
        logD("menuSize in insertNewMenu: ${currentCopiedMenu.size}")
        if (lastIndexOfCategory == -1) return
        currentCopiedMenu.forEach {category ->
            insertDishes(category, task)
        }
    }

    private fun insertDishes(
        category: Category,
        task: SimpleTask,
    ) {
        logD("lastIndexOfDish in ${category.name}")
        menuCollectionRef.document(category.name)
            .set(mapOf(category.name to category.dishes.size))
            .addOnSuccessListener {
                val lastIndex = category.dishes.lastIndex
                category.dishes.forEachIndexed { index, dish ->
                    menuCollectionRef.document(category.name).collection(COLLECTION_DISHES)
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

    private var enteredCount = 0
    private fun setNewMenuVersion(task: SimpleTask) {
        ++enteredCount
        logD("enteredCount: $enteredCount")
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
        ordersCollectionRef.get().addOnSuccessListenerWithDefaultFailureHandler(task) {
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