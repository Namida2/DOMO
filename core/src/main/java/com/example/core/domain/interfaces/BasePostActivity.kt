package com.example.core.domain.interfaces

interface BasePostActivity: EmployeeAuthCallback, LeaveAccountCallback {
    fun setOnNavigationItemSelectedListener()
    fun makeWorkerRequests()
    fun showNavigationUI()
    fun hideNavigationUI()
    fun observeOnNewPermissionEvent()
}