package extentions

import android.content.Context
import application.MyApplication
import di.AppComponent
import com.example.waiterCore.domain.Employee

val Context.appComponent: AppComponent
    get() = when (this) {
        is MyApplication -> _appComponent
        else -> this.applicationContext.appComponent
    }
var Context.employee: Employee?
    get() = when (this) {
        is MyApplication -> _employee
        else -> this.applicationContext.employee
    }
    set(value) = when (this) {
        is MyApplication -> _employee = value
        else -> this.applicationContext.employee = value
    }

