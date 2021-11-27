package com.example.domo.models.interfaces

import entities.Employee
import entities.PostItem
import entities.tools.TaskWithEmployee

interface RegistrationModelInterface {
    fun registration(employee: Employee, task: TaskWithEmployee)
    fun getPostItems(): MutableList<PostItem>
}