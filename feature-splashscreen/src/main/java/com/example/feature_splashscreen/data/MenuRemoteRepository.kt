package com.example.feature_splashscreen.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.feature_splashscreen.domain.MenuService
import com.example.waiter_core.data.database.daos.MenuDao
import com.example.waiter_core.domain.menu.Category
import com.example.waiter_core.domain.menu.Dish
import com.example.waiter_core.domain.tools.FirestoreReferences
import com.example.waiter_core.domain.tools.constants.FirestoreConstants
import com.example.waiter_core.domain.tools.extentions.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//TODO: Implement an observable pattern // STOPPED_0 //
class MenuRemoteRepositoryImpl @Inject constructor(
    override var menuService: MenuService,
) : MenuRemoteRepository {

    private val defaultMenuVersion = -1L
    private val menu: ArrayList<Category> = ArrayList()

    override fun readNewMenu(onComplete: (menuService: MenuService) -> Unit) {
        menuService.setMenuServiceStateAsLoading()
        FirestoreReferences.menuCollectionRef.get().addOnSuccessListener {
            val categoriesCount: Int = it.size()
            for (i in 0 until categoriesCount)
                if (i == categoriesCount - 1)
                    readDishes(it.documents[i].id, true, onComplete)
                else
                    readDishes(it.documents[i].id, onComplete = onComplete)
        }.addOnFailureListener {
            logE("$this: ${it.message}")
            onMenuLoadingFinish(onComplete)
        }
    }

    private fun readDishes(
        category: String,
        isItLastCategory: Boolean = false,
        onComplete: (menuService: MenuService) -> Unit,
    ) {
        val dishesCollectionRef =
            FirestoreReferences.menuCollectionRef.document(category)
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

    private fun onMenuLoadingFinish(onComplete: (menuService: MenuService) -> Unit) {
        menuService.setMenuServiceState(menu)
        onComplete(menuService)
    }

    override fun readMenuVersion(onComplete: (version: Long) -> Unit) {
        FirestoreReferences.menuDocumentRef.get().addOnSuccessListener {
            val menuVersion = it.data?.get(FirestoreConstants.FIELD_MENU_VERSION)
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
    var menuService: MenuService
    fun readNewMenu(onComplete: (menuService: MenuService) -> Unit)
    fun readMenuVersion(onComplete: (version: Long) -> Unit)
}