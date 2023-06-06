package com.fit2081.smstokenizer_w5.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface BookDao {
    @Query("select * from book")
    LiveData<List<Book>> getAllBook();

    @Query("select * from book where bookId=:name")
    List<Book> getBook(String name);

    @Insert
    void addBook(Book book);

    @Query("delete from book where title= :name")
    void deleteBook(String name);

    @Query("delete FROM book")
    void deleteAllBooks();

    @Query("SELECT * FROM book ORDER BY bookId DESC LIMIT 1")
    LiveData<Book> getLastBook();

    @Query("DELETE FROM book WHERE bookId = (SELECT bookId FROM book ORDER BY bookId DESC LIMIT 1)")
    void deleteLastBook();

    @Query("SELECT COUNT(*) FROM book")
    int getTotalBooks();

    @Query("SELECT COUNT(*) FROM book")
    LiveData<Integer> getBookCount();
}
