package com.fit2081.smstokenizer_w5.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository mRepository;
    private LiveData<List<Book>> mAllBooks;
    private LiveData<Book> mLastBook;

    public BookViewModel(@NonNull Application application) {
        super(application);
        mRepository = new BookRepository(application);
        mAllBooks = mRepository.getAllBooks();
        mLastBook = mRepository.getLastBook();
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


}
