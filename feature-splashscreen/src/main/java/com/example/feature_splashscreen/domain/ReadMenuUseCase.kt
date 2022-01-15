package com.example.feature_splashscreen.domain

import android.content.SharedPreferences
import com.example.feature_splashscreen.data.MenuLocalRepository
import com.example.feature_splashscreen.data.MenuRemoteRepository
import com.example.feature_splashscreen.data.UsersRemoteRepositiry
import com.example.waiter_core.data.database.daos.EmployeeDao
import com.example.waiter_core.domain.tools.constants.FirestoreConstants
import com.example.waiter_core.domain.tools.extentions.logD
import javax.inject.Inject

class ReadMenuUseCaseImpl @Inject constructor(
    private var sharedPreferences: SharedPreferences,
    private var menuLocalRepository: MenuLocalRepository,
    private var menuRemoteRepository: MenuRemoteRepository
): ReadMenuUseCase {

    override fun readMenu() {
        menuRemoteRepository.readMenuVersion { version ->
            if (isItTheSameMenuVersion(version)) {
                logD("$this: The same restaurant menu.")
                menuLocalRepository.readExitingMenu()
            } else {
                logD("$this: New restaurant menu.")
                menuRemoteRepository.readNewMenu {
                    sharedPreferences.edit().putLong(
                        FirestoreConstants.FIELD_MENU_VERSION, version).apply()
                    menuLocalRepository.insertCurrentMenu(it)
                }
            }
        }
    }
    private fun isItTheSameMenuVersion(version: Long): Boolean =
        sharedPreferences.getLong(FirestoreConstants.FIELD_MENU_VERSION, 0) == version

}

interface ReadMenuUseCase {
    fun readMenu()
}
