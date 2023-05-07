package com.example.hogotest.room.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hogotest.room.BookVO2;
import com.example.hogotest.room.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void  insert(User user);
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM book")
    List<BookVO2> getAllBooks();

}
