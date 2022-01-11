package com.example.feature_splashscreen.domain

import android.content.SharedPreferences
import com.example.feature_splashscreen.data.SplashScreenRemoteRepository
import com.example.waiter_core.data.database.daos.EmployeeDao
import javax.inject.Inject

class ReadMenuUseCase @Inject constructor(
    private var menuService: MenuService,
    private var employeeDao: EmployeeDao,
    private var sharedPreferences: SharedPreferences,
    private var remoteRepository: SplashScreenRemoteRepository,
) {

}