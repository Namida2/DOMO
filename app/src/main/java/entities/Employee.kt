package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class Employee (
    @PrimaryKey val email: String,
    var name: String,
    var post: String,
    var password: String,
    var permission: Boolean
)