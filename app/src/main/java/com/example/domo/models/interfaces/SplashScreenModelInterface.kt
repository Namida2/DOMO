package com.example.domo.models.interfaces

import com.example.waiterCore.domain.tools.TaskWithEmployee

interface SplashScreenModelInterface {
    fun getCurrentEmployee(task: TaskWithEmployee)
    fun readMenu()
}