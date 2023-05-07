package com.example.hogotest.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hogotest.myapp
import com.example.hogotest.room.dao.BookDao
import com.example.hogotest.room.dao.UserDao

@Database(
    entities = [Book::class,User::class],
    version = 2

)
abstract class AppDatabase():RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao

}
val db by lazy {
    Room.databaseBuilder(
        myapp,
        AppDatabase::class.java, "database-name"
    ).build()
}


