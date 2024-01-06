package com.example.hogotest.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hogotest.room.dao.BookDao
import com.example.hogotest.room.dao.UserDao
import com.hugo.mylibrary.components.application

@Database(
    entities = [Book::class,User::class],
    version = 1,
    exportSchema = true,)
abstract class AppDatabase():RoomDatabase(){
    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao

}
val db by lazy {
    Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).build()
}


