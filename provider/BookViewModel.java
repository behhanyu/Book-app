package com.fit2081.smstokenizer_w5.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mRepository;
    private LiveData<List<Book>> mAllBooks;
    private LiveData<Book> mLastBook;
    private MediatorLiveData<List<Book>> books;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getAllBooks();
        mLastBook = mRepository.getLastBook();
        books = new MediatorLiveData<>(); // can add source or remove source dynamically
        books.addSource(mRepository.getBooksByFilters(new SimpleSQLiteQuery("SELECT * FROM book")), bookResponse -> {
            books.setValue(bookResponse);
        });
    }

    public LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    public void insert(Book book) {
        mRepository.insert(book);
    }
    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void deleteLast(){
        mRepository.deleteLast();
    }

    public LiveData<Book> getLastBook() {
        return mRepository.getLastBook();
    }
    public LiveData<Integer> getBookCount() {
        return mRepository.getBookCount();
    }

    public LiveData<List<Book>> getFilteredBooks(String authorValue, String titleValue, String priceValue) {
        String query = "SELECT * FROM book";
        List<Object> queryArgs = new ArrayList<>();
        boolean containsConditions = false;

        if (!authorValue.isEmpty()) {
            query += " WHERE author = ?";
            queryArgs.add(authorValue);
            containsConditions = true;
        }

        if (!titleValue.isEmpty()) {
            if (containsConditions) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " title = ?";
            queryArgs.add(titleValue);
            containsConditions = true;
        }

        if (!priceValue.isEmpty()) {
            if (containsConditions) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " price = ?";
            queryArgs.add(priceValue);
            containsConditions = true;
        }

        query += ";";

        SimpleSQLiteQuery sqlQuery = new SimpleSQLiteQuery(query, queryArgs.toArray());

        books.removeSource(mRepository.getAllBooks());
        books.addSource(mRepository.getBooksByFilters(sqlQuery), bookResponse -> {
            books.setValue(bookResponse);
        });

        return books;
    }
}
