package com.example.silvertouchpracticaltest.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val mobileNumber: String,
    val email: String,
    val category: Long,
    val profilePicUri: String,
)
