package com.fit2081.smstokenizer_w5.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBook();
    }
    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }
    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBooks();
        });
    }

    void deleteLast(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteLastBook();
        });
    }

    LiveData<Book> getLastBook() {
        return mBookDao.getLastBook();
    }

    LiveData<Integer> getBookCount() {
        return mBookDao.getBookCount();
    }
    public LiveData<List<Book>> getBooksByFilters(SimpleSQLiteQuery filterQuery) {
        return mBookDao.getBooksByFilters(filterQuery);

    }

}
