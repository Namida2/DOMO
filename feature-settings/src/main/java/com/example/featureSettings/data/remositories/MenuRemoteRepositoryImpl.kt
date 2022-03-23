package com.example.featureSettings.data.remositories

import com.example.core.domain.menu.Category
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.FirestoreConstants
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_DISHES
import com.example.core.domain.tools.constants.FirestoreReferences
import com.example.core.domain.tools.constants.FirestoreReferences.menuCollectionRef
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    override fun saveNewMenu(task: SimpleTask) {
        insertNewMenu(task)
    }

    private fun deleteOldMenu(task: SimpleTask) {
        menuCollectionRef.get().addOnSuccessListener {
            val lastIndex = it.documents.lastIndex
            it.documents.forEachIndexed { index, category ->
                if (index == lastIndex) deleteAllDishes(category.id, task, true)
                else deleteAllDishes(category.id, task)
            }
        }.addOnFailureListener {
            task.onError(it.getExceptionMessage())
        }
    }

    private fun deleteAllDishes(
        categoryId: String,
        task: SimpleTask,
        isLastCategory: Boolean = false
    ) {
        menuCollectionRef.document(categoryId).collection(COLLECTION_DISHES).get()
            .addOnSuccessListener {
                val lastIndex = it.documents.lastIndex
                it.documents.forEachIndexed { index, dish ->
                    menuCollectionRef.document(categoryId).collection(COLLECTION_DISHES)
                        .document(dish.id).delete().addOnCanceledListener {
                            if (index == lastIndex) deleteCategory(categoryId, task)
                            if (index == lastIndex && isLastCategory) insertNewMenu(task)
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }
            }.addOnFailureListener {
                task.onError(it.getExceptionMessage())
            }
    }

    private fun deleteCategory(categoryId: String, task: SimpleTask) {

    }

    private fun insertNewMenu(task: SimpleTask) {
        val lastIndexOfCategory = MenuService.menu.lastIndex
        MenuService.menu.forEachIndexed { index, category ->
            if (lastIndexOfCategory == index) insertDishes(category, task, true)
            else insertDishes(category, task)
        }
    }

    private fun insertDishes(
        category: Category,
        task: SimpleTask,
        isLastDocument: Boolean = false
    ) {
        category.dishes.forEachIndexed { index, dish ->
            val lastIndexOfDish = category.dishes.lastIndex
            FirestoreReferences.fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
                .document(FirestoreConstants.DOCUMENT_DOMO)
                .collection("menuTest").document(dish.name).collection(COLLECTION_DISHES)
                .document(dish.name).set(dish).addOnSuccessListener {
                    if (lastIndexOfDish == index && isLastDocument) task.onSuccess(Unit)
                }.addOnFailureListener {
                    task.onError(it.getExceptionMessage())
                }
        }
    }
}