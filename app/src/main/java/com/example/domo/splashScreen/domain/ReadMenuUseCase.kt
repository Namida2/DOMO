package com.example.domo.splashScreen.domain

import android.content.SharedPreferences
import com.example.domo.splashScreen.data.MenuLocalRepository
import com.example.domo.splashScreen.data.MenuRemoteRepository
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
