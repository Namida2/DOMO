package com.example.core.domain.repositories

import com.example.core.domain.entities.tools.TaskWithPassword

interface AdminPasswordRemoteRepository {
    fun getPassword(task: TaskWithPassword)
}