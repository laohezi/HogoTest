package com.example.hogotest

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.hogotest.room.AppDatabase
import com.example.hogotest.room.Book
import com.example.hogotest.room.User
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
@RunWith(AndroidJUnit4::class)
class RoomTest {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    val db = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, "database-name"
    ).build()


    @Test
    fun insertUser(){
        val userDao = db.userDao()
        val user = User()
        user.name = "test"
        user.age = 10
        userDao.insert(user)
    }

    @Test
    fun insertBook(){
        val bookDao = db.bookDao()
        val book = Book()
        book.name = "三体"
        book.author = "刘慈欣"
        book.student_id = 0

        bookDao.insert(book)
    }





}



