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

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private lateinit var currentCopiedMenu: MutableList<Category>

    override fun saveNewMenu(task: SimpleTask) {
        currentCopiedMenu = MenuService.copyMenu()
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
            val lastIndex = it.documents.lastIndex
            if (lastIndex == -1) insertNewMenu(task)
            logD("lastIndexOfDocument in $lastIndex")
            it.documents.forEachIndexed { index, category ->
                if (index == lastIndex) deleteAllDishes(category.id, task, true)
                else deleteAllDishes(category.id, task)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun deleteAllDishes(
        categoryName: String,
        task: SimpleTask,
        isLastCategory: Boolean = false
    ) {
        menuCollectionRef.document(categoryName).collection(COLLECTION_DISHES).get()
            .addOnSuccessListener {
                logD(categoryName)
                val lastIndex = it.documents.lastIndex
                if (lastIndex == -1) onCategoryDeleted(categoryName, task)
                logD("lastIndexOfDish in $lastIndex")
                it.documents.forEachIndexed { index, dish ->
                    menuCollectionRef.document(categoryName).collection(COLLECTION_DISHES)
                        .document(dish.id).delete().addOnSuccessListener {
                            logD("delete ${dish.id}")
                            if (index == lastIndex) onCategoryDeleted(categoryName, task)
                            if (index == lastIndex && isLastCategory)
                                insertNewMenu(task)
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }

    //TODO: Delete empty orders in the end(?) //STOPPED//
    private fun onCategoryDeleted(categoryName: String, task: SimpleTask) {
        val category = currentCopiedMenu.find {
            it.name == categoryName
        } ?: menuCollectionRef.document(categoryName).delete().also { return }
        currentCopiedMenu.remove(category)
        insertDishes(category as Category, task, currentCopiedMenu.isEmpty())
    }

    private fun insertNewMenu(task: SimpleTask) {
        val lastIndexOfCategory = currentCopiedMenu.lastIndex
        logD("menu: $MenuService")
        if (lastIndexOfCategory == -1)  return
        currentCopiedMenu.forEachIndexed { index, category ->
            if (lastIndexOfCategory == index) insertDishes(category, task, true)
            else insertDishes(category, task)
        }
    }

    private fun insertDishes(
        category: Category,
        task: SimpleTask,
        isLastDocument: Boolean = false
    ) {
        logD("lastIndexOfDish in ${category.name}")
        category.dishes.forEachIndexed { index, dish ->
            val lastIndexOfDish = category.dishes.lastIndex
            menuCollectionRef.document(category.name)
                .set(mapOf(category.name to category.dishes.size))
                .addOnSuccessListener {
                    menuCollectionRef.document(category.name).collection(COLLECTION_DISHES)
                        .document(dish.name).set(dish).addOnSuccessListener {
                            logD("set ${dish.name}")
                            if (lastIndexOfDish == index && isLastDocument) {
                                logD("insertDishes::onSuccess")
                                setNewMenuVersion(task)
                            }
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }.addOnFailureListener {
                    task.onError(it.getExceptionMessage())
                }
        }
    }

    private fun setNewMenuVersion(task: SimpleTask) {
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
            if(lastIndex == -1) onComplete()
            it.result.forEachIndexed { index, orderId ->
                ordersCollectionRef.document(orderId.id).delete().addOnSuccessListenerWithDefaultFailureHandler(task) {
                    if(index == lastIndex) onComplete()
                }
            }
        }
    }
}