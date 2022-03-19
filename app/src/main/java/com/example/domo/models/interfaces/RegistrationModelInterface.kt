package com.example.domo.models.interfaces

import com.example.core.domain.entities.Employee
import com.example.featureRegistration.domain.PostItem

interface RegistrationModelInterface {
    fun registration(employee: Employee, task: com.example.core.domain.tools.TaskWithEmployee)
    fun getPostItems(): MutableList<PostItem>
}