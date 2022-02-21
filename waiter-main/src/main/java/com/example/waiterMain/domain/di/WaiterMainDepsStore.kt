package com.example.waiterMain.domain.di

import com.example.waiterCore.domain.Employee
import com.example.waiterCore.domain.tools.constants.OtherStringConstants.CURRENT_EMPLOYEE_IS_NOT_SET
import java.lang.IllegalStateException

object WaiterMainDepsStore {
    lateinit var deps: WaiterMainDeps
    var currentEmployee: Employee? = null
    get() = when (field) {
            null -> throw IllegalStateException(CURRENT_EMPLOYEE_IS_NOT_SET)
            else -> field
        }
    val appComponent: WaiterMainAppComponent by lazy {
        DaggerWaiterMainAppComponent.builder().provideWaiterMainDeps(deps).build()
    }
}
