package com.example.waiterMain.domain.di

import com.example.core.domain.interfaces.BaseDepsStore
import com.example.core.domain.interfaces.EmployeeAuthCallback
import com.example.core.domain.interfaces.NewMenuVersionCallback
import com.example.core.domain.interfaces.OnNetworkConnectionLostCallback
import com.google.firebase.auth.FirebaseAuth

object WaiterMainDepsStore : BaseDepsStore {
    var deps: WaiterMainDeps? = null
    var profileDeps: ProfileModuleDeps? = null
    var employeeAuthCallback: EmployeeAuthCallback? = null
    var newMenuVersionCallback: NewMenuVersionCallback? = null
    var onNetworkConnectionLostCallback: OnNetworkConnectionLostCallback? = null
    var appComponent: WaiterMainAppComponent? = null
        get() = if (field == null) {
            field = DaggerWaiterMainAppComponent.builder().provideWaiterMainDeps(deps!!).build(); field
        } else field

    override fun onCleared() {
        deps = null
        profileDeps = null
        employeeAuthCallback = null
        newMenuVersionCallback = null
        appComponent = null
        onNetworkConnectionLostCallback = null
    }

}

interface ProfileModuleDeps {
    val firebaseAuth: FirebaseAuth
}
