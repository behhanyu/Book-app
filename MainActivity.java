package com.fit2081.smstokenizer_w5;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.smstokenizer_w5.provider.Book;
import com.fit2081.smstokenizer_w5.provider.BookDao;
import com.fit2081.smstokenizer_w5.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private EditText etBookID,etTitle,etISBN,etAuthor,etDescription,etPrice;

    //     Week 5
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    private BookViewModel mBookViewModel;
    BookDao mBookDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main);
        Log.i("lab3", "onCreate");

        etBookID = findViewById(R.id.etBookID);
        etTitle = findViewById(R.id.etTitle);
        etISBN = findViewById(R.id.etISBN);
        etAuthor = findViewById(R.id.etAuthor);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);

        // Week 6
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBook();
            }
        });

        // Week 7
        mBookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        Frag1 fragment = new Frag1();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

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

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            int id = item.getItemId();
            if (id == R.id.add_book) {
                // Do something
                addBook();
            } else if (id == R.id.remove_last_book) {
                // remove the last item from recycler view
                mBookViewModel.deleteLast();
            } else if (id == R.id.remove_all_books) {
                // remove all items from recycler view
                mBookViewModel.deleteAll();
            } else if (id == R.id.list_all) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            } else if (id == R.id.close) {
                // use the finish method to close the activity
                finish();
            } else if (id == R.id.delete_unknown_author) {
                // use the finish method to close the activity
                mBookViewModel.deleteUnknownAuthorBooks();
            }
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_fields) {
            clearText();
        } else if (id == R.id.load_data) {
            loadData();
        } else if(id == R.id.total_books) {
            mBookViewModel.getBookCount().observe(this, count -> {
                // Display the book count
                Toast.makeText(this, "Total Books: " + count, Toast.LENGTH_SHORT).show();
            });
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadData(){
        mBookViewModel.getLastBook().observe(this, new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                if (book != null) {
                    etBookID.setText(String.valueOf(book.getBookId()));
                    etTitle.setText(book.getTitle());
                    etISBN.setText(book.getIsbn());
                    etAuthor.setText(book.getAuthor());
                    etDescription.setText(book.getDescription());
                    etPrice.setText(book.getPrice());
                }
            }
        });
    }

    public void addBook(){
//        saveData();

        String bookTitle = etTitle.getText().toString();
        String bookPrice = etPrice.getText().toString();
        Toast myMessage = Toast.makeText(this, String.format("Book (%s) and the price (%s)", bookTitle, bookPrice), Toast.LENGTH_SHORT);
        myMessage.show();
        // show the book in the recycler view
        Book book = new Book(etTitle.getText().toString(), etISBN.getText().toString(), etAuthor.getText().toString(), etDescription.getText().toString(), etPrice.getText().toString());

//        data.add(book);
        mBookViewModel.insert(book);
    }

    public void clearText() {
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
        super.onSaveInstanceState(outState);
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

    class MyBroadCastReceiver extends BroadcastReceiver {
        /*
         * This method 'onReceive' will get executed every time class SMSReceive sends a broadcast
         * */
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * Retrieve the message from the intent
             * */
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            Toast.makeText(context, intent.getStringExtra(SMSReceiver.SMS_MSG_KEY), Toast.LENGTH_SHORT).show();
            /*
             * String Tokenizer is used to parse the incoming message
             * The protocol is to have the account holder name and account number separate by a semicolon
             * */

            StringTokenizer sT = new StringTokenizer(msg, "|");
            String inputBookID = sT.nextToken();
            String inputTitle = sT.nextToken();
            String inputISBN = sT.nextToken();
            String inputAuthor = sT.nextToken();
            String inputDescription = sT.nextToken();
            double inputPrice = Double.parseDouble(sT.nextToken());


            //update the UI
            etBookID.setText(inputBookID);
            etTitle.setText(inputTitle);
            etISBN.setText(inputISBN);
            etAuthor.setText(inputAuthor);
            etDescription.setText(inputDescription);
            etPrice.setText(String.format("%.2f", inputPrice));
        }
    }
}
