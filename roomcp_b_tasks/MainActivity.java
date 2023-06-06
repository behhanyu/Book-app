package com.fit2081.roomcp_b_tasks;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    TextView tV;
    Button button, deleteButton,  addButton;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tV = findViewById(R.id.textView_id);
        button = findViewById(R.id.book_count_button);
        deleteButton = findViewById(R.id.delete_button);
        addButton = findViewById(R.id.add_button);
        Uri uri = Uri.parse("content://fit2081.app.hanyu/book");
//        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("book");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor result = getContentResolver().query(uri, null, null, null);
                if (result != null)
                    tV.setText(result.getCount() + "");
                else
                    tV.setText("Result is null");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(uri, null, null);

                myRef.removeValue();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                String bookTitle = "Default";
                String bookIsbn = "XXX";
                String bookAuthor = "Unknown";
                String bookDescription = "Book";
                int bookPrice = 10;

                values.put("title", bookTitle);
                values.put("isbn", bookIsbn);
                values.put("author", bookAuthor);
                values.put("description", bookDescription);
                values.put("price", bookPrice);
                Uri uri2 = getContentResolver().insert(uri, values);
                Toast.makeText(MainActivity.this, uri2.toString(), Toast.LENGTH_LONG).show();
                Book book = new Book(bookTitle, bookIsbn, bookAuthor, bookDescription, bookPrice);

                myRef.push().setValue(book);
            }
        });

    }

}