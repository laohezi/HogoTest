package com.example.hogotest.room

import androidx.room.Embedded
import androidx.room.Relation

data class BookVO2(


    @Relation(
        parentColumn = "student_id",
        entityColumn = "id"
    )
    val user: User,
    @Embedded
    val books: Book?

)