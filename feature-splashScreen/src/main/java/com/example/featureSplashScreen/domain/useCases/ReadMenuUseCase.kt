package com.example.featureSplashScreen.domain.useCases

import android.content.SharedPreferences
import com.example.core.domain.tools.constants.FirestoreConstants
import com.example.core.domain.tools.extensions.logD
import com.example.featureSplashScreen.domain.repositories.MenuLocalRepository
import com.example.featureSplashScreen.domain.repositories.MenuRemoteRepository
import javax.inject.Inject

class ReadMenuUseCaseImpl @Inject constructor(
    private var sharedPreferences: SharedPreferences,
    private var menuLocalRepository: MenuLocalRepository,
    private var menuRemoteRepository: MenuRemoteRepository
) : ReadMenuUseCase {

    override fun readMenu() {
        menuRemoteRepository.readMenuVersion { version ->
            if (isItTheSameMenuVersion(version)) {
                logD("$this: The same restaurant menu.")
                menuLocalRepository.readExitingMenu()
            } else {
                logD("$this: New restaurant menu.")
                menuRemoteRepository.readNewMenu {
                    sharedPreferences.edit().putLong(
                        FirestoreConstants.FIELD_MENU_VERSION, version
                    ).apply()
                    menuLocalRepository.insertCurrentMenu()
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
