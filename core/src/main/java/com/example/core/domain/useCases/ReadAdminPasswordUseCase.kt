package com.example.core.domain.useCases

import com.example.core.domain.entities.tools.ErrorMessage
import com.example.core.domain.entities.tools.TaskWithPassword
import com.example.core.domain.repositories.AdminPasswordRemoteRepository
import javax.inject.Inject

class ReadAdminPasswordUseCase @Inject constructor(
    private val adminPasswordRepository: AdminPasswordRemoteRepository
) {
    private var password: String? = null
    fun getPassword(task: TaskWithPassword) {
        if(password == null)
            adminPasswordRepository.getPassword(object : TaskWithPassword {
                override fun onSuccess(result: String) {
                    password = result
                    task.onSuccess(result)
                }
                override fun onError(message: ErrorMessage?) { task.onError()}
            })
        else task.onSuccess(password!!)
    }

}