package com.example.core.domain.useCases

import android.content.SharedPreferences
import com.example.core.domain.entities.menu.MenuService
import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.SimpleTask
import com.example.core.domain.entities.tools.constants.FirestoreConstants
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.repositories.MenuLocalRepository
import com.example.core.domain.repositories.MenuRemoteRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class ReadMenuUseCase @Inject constructor(
    private var sharedPreferences: SharedPreferences,
    private var menuLocalRepository: MenuLocalRepository,
    private var menuRemoteRepository: MenuRemoteRepository
) {

    fun readMenu(menuCollectionRef: CollectionReference, getFromServer: Boolean,  task: SimpleTask) {
        MenuService.resetState()
        if (getFromServer) {
            getFromServer(menuCollectionRef, getFromServer, null, task)
            return
        }
        menuRemoteRepository.readMenuVersion { version ->
            if (isItTheSameMenuVersion(version)) {
                logD("$this: The same restaurant menu.")
                menuLocalRepository.readExitingMenu()
            } else {
                logD("$this: New restaurant menu.")
                getFromServer(menuCollectionRef, getFromServer, version, task)
            }
        }
    }

    private fun getFromServer(menuCollectionRef: CollectionReference, getFromServer: Boolean, menuVersion: Long?, task: SimpleTask) {
        menuRemoteRepository.readNewMenu(menuCollectionRef, object : SimpleTask {
            override fun onSuccess(result: Unit) {
                if (!getFromServer) {
                    menuLocalRepository.insertCurrentMenu {
                        sharedPreferences.edit().putLong(
                            FirestoreConstants.FIELD_MENU_VERSION, menuVersion!!
                        ).apply()
                    }
                }
                task.onSuccess(Unit)
            }

            override fun onError(message: ErrorMessage?) {
                task.onError(message)
            }
        })
    }

    private fun isItTheSameMenuVersion(version: Long): Boolean =
        sharedPreferences.getLong(FirestoreConstants.FIELD_MENU_VERSION, 0) == version

}

