package application

import android.app.Application
import android.content.Context
import di.AppComponent
import di.DaggerAppComponent


class MyApplication: Application() {
    val _appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
val Context.appComponent: AppComponent
get() = when(this) {
    is MyApplication -> _appComponent
    else -> this.applicationContext.appComponent
}
