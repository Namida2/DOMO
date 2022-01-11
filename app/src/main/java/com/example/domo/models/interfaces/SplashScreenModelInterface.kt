package com.example.domo.models.interfaces

import com.example.waiter_core.domain.tools.TaskWithEmployee

interface SplashScreenModelInterface {
    fun getCurrentEmployee(task: TaskWithEmployee)
    fun readMenu()
}