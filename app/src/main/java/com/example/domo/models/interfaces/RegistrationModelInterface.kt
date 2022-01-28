package com.example.domo.models.interfaces

import entities.PostItem
import com.example.waiterCore.domain.tools.TaskWithEmployee
import com.example.waiterCore.domain.Employee

interface RegistrationModelInterface {
    fun registration(employee: Employee, task: TaskWithEmployee)
    fun getPostItems(): MutableList<PostItem>
}