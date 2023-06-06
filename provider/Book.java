package com.fit2081.smstokenizer_w5.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "bookId")
    private int bookId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "isbn")
    private String isbn;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "price")
    private String price;



    public Book(String title, String isbn, String author, String description, String price) {

        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.description = description;
        this.price = price;

    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public void setBookId(@NonNull int bookId) {
        this.bookId = bookId;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public void setIsbn(@NonNull String isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(@NonNull String author) {
        this.author = author;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setPrice(@NonNull String price) {
        this.price = price;
    }

}
