package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee")
data class Employee (
    @PrimaryKey val id: Int,
    var name: String,
    val email: String
)