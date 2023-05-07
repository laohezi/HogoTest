package com.example.hogotest.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String?,
    var email: String?,
    var age: Int
)