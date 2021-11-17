package com.example.domo.models.remoteRepository.interfaces

import com.google.firebase.auth.FirebaseUser
import entities.Dish
import entities.Employee
import entities.Task

interface SSRemoteRepositoryInterface {
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
    fun readNewMenu(task: Task<List<Dish>, Unit>)
    fun readMenuVersion(task: Task<Long, Unit>)
    fun readCurrentEmployee (email: String, onComplete: (employee: Employee?) -> Unit)
}