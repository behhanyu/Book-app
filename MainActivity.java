package com.example.w2lab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText etBookID,etTitle,etISBN,etAuthor,etDescription,etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("lab3", "onCreate");

        etBookID = findViewById(R.id.etBookID);
        etTitle = findViewById(R.id.etTitle);
        etISBN = findViewById(R.id.etISBN);
        etAuthor = findViewById(R.id.etAuthor);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);

        // Price Edittext can only accept 2dp
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String pattern = "^\\d+(\\.\\d{0,2})?$";
            String input = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length());
            if (input.matches(pattern)) {
                return null;
            }
            return "";
        };

        etPrice.setFilters(new InputFilter[]{filter});
    }
    public void DoublePrice(View v){
        EditText doubledPrice = findViewById(R.id.etPrice);
        String currentValue = doubledPrice.getText().toString();
        if (currentValue.isEmpty()) {return;}
        double doubledValue = Double.parseDouble(currentValue) * 2;
        doubledPrice.setText(String.format(Locale.US, "%.2f", doubledValue));
    }

    public void setISBN(View v){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("isbn", "00112233");
        editor.apply();
    }
    public void saveData(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", etBookID.getText().toString());
        editor.putString("title", etTitle.getText().toString());
        editor.putString("isbn", etISBN.getText().toString());
        editor.putString("author", etAuthor.getText().toString());
        editor.putString("description", etDescription.getText().toString());
        editor.putString("price", etPrice.getText().toString());
        editor.apply();
    }

    public void loadData(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", 0);
        etBookID.setText(prefs.getString("id", ""));
        etTitle.setText(prefs.getString("title", ""));
        etISBN.setText(prefs.getString("isbn", ""));
        etAuthor.setText(prefs.getString("author", ""));
        etDescription.setText(prefs.getString("description", ""));
        etPrice.setText(prefs.getString("price", ""));
    }
    public void loadBook(View v) {
        loadData();
    }

    public void addBook(View v){
        saveData();

        String bookTitle = etTitle.getText().toString();
        String bookPrice = etPrice.getText().toString();
        Toast myMessage = Toast.makeText(this, String.format("Book (%s) and the price (%s)", bookTitle, bookPrice), Toast.LENGTH_SHORT);
        myMessage.show();
    }

    public void clearText(View v) {
        etBookID.getText().clear();
        etTitle.getText().clear();
        etISBN.getText().clear();
        etAuthor.getText().clear();
        etDescription.getText().clear();
        etPrice.getText().clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lab3", "onStart");

        loadData();
    }

    //onResume, onPause, onStop(saveData), onDestroy

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("title", etTitle.getText().toString());
        outState.putString("isbn", etISBN.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        etTitle.setText(savedInstanceState.getString("title"));
        etISBN.setText(savedInstanceState.getString("isbn"));
        etBookID.setText("");
        etAuthor.setText("");
        etDescription.setText("");
        etPrice.setText("");
    }
}