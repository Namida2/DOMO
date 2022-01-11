package com.example.domo.models.interfaces

import com.example.waiter_core.domain.tools.TaskWithEmployee

interface LogInModelInterface {
    fun signIn(email: String, password: String, task: TaskWithEmployee)
}