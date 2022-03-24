package com.example.featureSettings.data.remositories

import com.example.core.domain.menu.Category
import com.example.core.domain.menu.MenuService
import com.example.core.domain.tools.SimpleTask
import com.example.core.domain.tools.constants.FirestoreConstants.COLLECTION_DISHES
import com.example.core.domain.tools.constants.FirestoreReferences.menuCollectionRef
import com.example.core.domain.tools.extensions.getExceptionMessage
import com.example.core.domain.tools.extensions.logD
import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import javax.inject.Inject

class MenuRemoteRepositoryImpl @Inject constructor() : MenuRemoteRepository {

    private lateinit var currentCopiedMenu: MutableList<Category>

    override fun saveNewMenu(task: SimpleTask) {
        currentCopiedMenu = MenuService.copyMenu()
        deleteOldMenu(task)
//        insertNewMenu(task)
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

    private fun onCategoryDeleted(categoryName: String, task: SimpleTask) {
        val category = currentCopiedMenu.find {
            it.name == categoryName
        }?.copy() ?: menuCollectionRef.document(categoryName).delete().also { return }
        currentCopiedMenu.remove(category)
        insertDishes(category as Category, task, currentCopiedMenu.isEmpty())
    }

    private fun insertNewMenu(task: SimpleTask) {
        val lastIndexOfCategory = currentCopiedMenu.lastIndex
        logD("menu: $MenuService")
        if (lastIndexOfCategory == -1) task.onSuccess(Unit).also { return }
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
        category.dishes.forEachIndexed { index, dish ->
            val lastIndexOfDish = category.dishes.lastIndex
            logD("lastIndexOfDish in ${category.name}")
            menuCollectionRef.document(category.name)
                .set(mapOf(category.name to category.dishes.size))
                .addOnSuccessListener {
                    menuCollectionRef.document(category.name).collection(COLLECTION_DISHES)
                        .document(dish.name).set(dish).addOnSuccessListener {
                            logD("set ${dish.name}")
                            if (lastIndexOfDish == index && isLastDocument) task.onSuccess(Unit)
                        }.addOnFailureListener { exception ->
                            task.onError(exception.getExceptionMessage())
                        }
                }.addOnFailureListener {
                    task.onError(it.getExceptionMessage())
                }
        }
    }
}