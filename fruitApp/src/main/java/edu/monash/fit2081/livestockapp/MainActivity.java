package edu.monash.fit2081.livestockapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<FruitItem> dataItems = new ArrayList<>(); // Declare the list of items outside of makeRequest()
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Fruit Application");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        et = findViewById(R.id.et);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fruitEntered = et.getText().toString(); // Use a local variable to get the entered fruit
                makeRequest(fruitEntered);
            }
        });
    }

    private void makeRequest(String fruitEntered) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://fruityvice.com/api/fruit/" + fruitEntered;

        JsonObjectRequest stringRequest =
                new JsonObjectRequest(Request.Method.GET, url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String name = response.getString("name");
                                    String family = "Family: "+response.getString("family");
                                    JSONObject nutritions = response.getJSONObject("nutritions");
                                    String calories = "Calories: " + nutritions.getString("calories");
                                    String sugar = "Sugar: " + nutritions.getString("sugar");
                                    String carbohydrates = "Carbohydrates: "+nutritions.getString("carbohydrates");
                                    String protein = "Protein: "+nutritions.getString("protein");

                                    FruitItem fruitItem = new FruitItem(name, family, calories, sugar, carbohydrates, protein);

                                    dataItems.add(fruitItem); // Add the new item to the existing list

                                    if (adapter == null) { // Only create a new adapter if it doesn't exist
                                        adapter = new RecyclerAdapter(dataItems);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                                    }
                                } catch (Exception e) {
                                    Log.d("stock", e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("stock", error.getMessage());
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}
