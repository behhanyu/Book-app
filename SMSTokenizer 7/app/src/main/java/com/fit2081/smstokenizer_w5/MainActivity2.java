package com.fit2081.smstokenizer_w5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity2 extends AppCompatActivity {

    private Frag1 fragment;
    String authorValue;
    String titleValue;
    String priceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        fragment = new Frag1();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame2, fragment)
                .commit();

        EditText filterAuthor = findViewById(R.id.filterAuthor);
        EditText filterTitle = findViewById(R.id.filterTitle);
        EditText filterPrice = findViewById(R.id.filterPrice);
        Button buttonFilter = findViewById(R.id.btFilter);
        Button buttonClearFilter = findViewById(R.id.btClearFilter);

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorValue = filterAuthor.getText().toString();
                titleValue = filterTitle.getText().toString();
                priceValue = filterPrice.getText().toString();

                fragment.applyFilter(authorValue, titleValue, priceValue);
            }
        });

        buttonClearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear all filters
                filterAuthor.setText("");
                filterTitle.setText("");
                filterPrice.setText("");


                fragment.applyFilter("","","");
            }
        });
    }
}