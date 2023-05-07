package com.example.hogotest.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var student_id: Int,
    var name: String?,
    var author: String?,
    var pages: Int,
)