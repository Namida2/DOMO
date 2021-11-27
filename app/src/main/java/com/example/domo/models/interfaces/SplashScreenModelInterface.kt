package com.example.domo.models.interfaces

import entities.tools.TaskWithEmployee

interface SplashScreenModelInterface {
    fun getCurrentEmployee(task: TaskWithEmployee)
    fun readMenu()
}