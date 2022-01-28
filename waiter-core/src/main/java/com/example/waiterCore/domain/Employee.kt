package com.example.waiterCore.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class Employee (
    @PrimaryKey var email: String,
    var name: String,
    var post: String,
    var password: String,
    var permission: Boolean = false
) {
    constructor() : this("", "", "", "")
}