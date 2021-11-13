package com.example.domo.models.interfaces

import entities.TaskWithEmployee

interface LogInModelInterface {
    fun signIn(email: String, password: String, task: TaskWithEmployee)
}