package com.example.domo.models.interfaces

import com.example.core.domain.tools.TaskWithEmployee

interface LogInModelInterface {
    fun signIn(email: String, password: String, task: com.example.core.domain.tools.TaskWithEmployee)
}