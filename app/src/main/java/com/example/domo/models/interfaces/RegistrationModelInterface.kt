package com.example.domo.models.interfaces

import entities.PostItem
import com.example.waiter_core.domain.tools.TaskWithEmployee
import com.example.waiter_core.domain.Employee

interface RegistrationModelInterface {
    fun registration(employee: Employee, task: TaskWithEmployee)
    fun getPostItems(): MutableList<PostItem>
}