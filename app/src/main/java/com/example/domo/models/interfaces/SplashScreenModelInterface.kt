package com.example.domo.models.interfaces

import entities.TaskWithEmployee

interface SplashScreenModelInterface {
    fun getCurrentEmployee(task: TaskWithEmployee)
    fun readMenu()
}