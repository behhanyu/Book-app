package com.fit2081.smstokenizer_w5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Frag1 fragment = new Frag1();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame2, fragment)
                .commit();
    }
}