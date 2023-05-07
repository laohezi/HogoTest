package com.example.hogotest.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hogotest.room.Book;
import com.example.hogotest.room.BookVO;
import com.example.hogotest.room.BookVO2;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    public void insert(Book book);

    @Query("SELECT * FROM Book WHERE id = :id")
    public Book getBook(int id);

    @Query("SELECT book.id as id ," +
            "book.name as name," +
            "book.author as author," +
            "book.pages as pages," +
            "book.student_id as student_id," +
            "user.email as email" +
            " FROM  book , user WHERE book.student_id = user.id")
    List<BookVO> getAll();


    @Query("SELECT * FROM book where book.author like :searchBox or book.name like :searchBox")
    List<BookVO2> getAllBooks(String searchBox);

}
