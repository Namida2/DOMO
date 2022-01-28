package com.example.domo.models.interfaces

import com.example.waiterCore.domain.tools.TaskWithEmployee

interface LogInModelInterface {
    fun signIn(email: String, password: String, task: TaskWithEmployee)
}