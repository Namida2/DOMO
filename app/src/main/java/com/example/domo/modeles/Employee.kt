package com.example.domo.modeles

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee (
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val permission: Boolean,
    val post: String
    ) {}