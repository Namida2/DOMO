package com.example.domo.models.interfaces

import entities.PostItem
import entities.tools.TaskWithEmployee
import entities.Employee

interface RegistrationModelInterface {
    fun registration(employee: Employee, task: TaskWithEmployee)
    fun getPostItems(): MutableList<PostItem>
}