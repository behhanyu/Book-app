package com.fit2081.smstokenizer_w5;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

import com.fit2081.smstokenizer_w5.provider.Book;
import com.fit2081.smstokenizer_w5.provider.BookDao;
import com.fit2081.smstokenizer_w5.provider.BookViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    private EditText etBookID,etTitle,etISBN,etAuthor,etDescription,etPrice;

    //     Week 5
    View constraint_layout;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;
    private BookViewModel mBookViewModel;
    DatabaseReference myRef;
    View myFrame;
    int x_down;
    int y_down;
    GestureDetector gestureDetector;

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

        // Week 11
        gestureDetector = new GestureDetector(this, new MyGestureDetector());

        constraint_layout = findViewById(R.id.constraint_id);
        constraint_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // Week 10
//        myFrame = findViewById(R.id.touch_frame_id);
//        myFrame.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent motionEvent) {
//                int action = motionEvent.getActionMasked();
//                switch(action) {
//                    case MotionEvent.ACTION_DOWN:
//                        x_down = (int) motionEvent.getX();
//                        y_down = (int) motionEvent.getY();
//                        return true;
//
//                    case MotionEvent.ACTION_UP:
//                        if (Math.abs(y_down - motionEvent.getY()) < 60) {
//                            if (x_down - motionEvent.getX() > 0) { // right to left
//                                addBook();
//                                return true;
//                            }
//                        } else if (Math.abs(x_down - motionEvent.getX()) < 60) {
//                            if (y_down - motionEvent.getY() > 0) { // bottom to top
//                                clearText();
//                                return true;
//                            }
//                        }
//                    case MotionEvent.ACTION_MOVE:
////                        etAuthor.setText(etAuthor.getText().toString().toUpperCase());
//                        if (Math.abs(y_down - motionEvent.getY()) < 60) {
//                            if (x_down - motionEvent.getX() < 0) { // left to right
//                                Double currentPrice = Double.parseDouble(etPrice.getText().toString());
//                                currentPrice += 1;
//                                etPrice.setText(String.valueOf(currentPrice));
//                                return true;
//                            }
//                        }
//                    default:
//                        return false;
//                }
//            }
//
//
//        });


        // Week 8

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("book");


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

    // Week 11
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            clearText();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
            String newISBN = RandomString.generateNewRandomString(8);
            etISBN.setText(newISBN);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            if (velocityX > 1000 || velocityY > 1000) {
                moveTaskToBack(true);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            loadData();
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > Math.abs(distanceY)) { // check if scrolling horizontally
                Double currentPrice = Double.parseDouble(etPrice.getText().toString());
                if (distanceX < 0) { // scrolling left to right (previous e2 - current e2)
                    currentPrice -= (int)distanceX;
                    etPrice.setText(String.valueOf(currentPrice));
                } else { // scrolling right to left
                    currentPrice += (int)distanceY;
                    etPrice.setText(String.valueOf(currentPrice));
                }
            }
            if (Math.abs(distanceX) < Math.abs(distanceY)) {
                etTitle.setText("untitled");
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
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
                // Use a query to retrieve the last book added to the database
                Query lastBookQuery = myRef.orderByChild("timestamp").limitToLast(1);
                lastBookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get the key of the last book
                        String lastBookKey = dataSnapshot.getChildren().iterator().next().getKey();

                        // Delete the last book from the database
                        myRef.child(lastBookKey).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle the error
                    }
                });
            } else if (id == R.id.remove_all_books) {
                // remove all items from recycler view
                mBookViewModel.deleteAll();
                myRef.removeValue();
            } else if (id == R.id.list_all) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            } else if (id == R.id.close) {
                // use the finish method to close the activity
                finish();
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
                    etPrice.setText(String.format("%.2f", book.getPrice()));
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
        Book book = new Book(etTitle.getText().toString(), etISBN.getText().toString(), etAuthor.getText().toString(), etDescription.getText().toString(), Double.valueOf(etPrice.getText().toString()));

//        data.add(book);
        mBookViewModel.insert(book);
        myRef.push().setValue(book);
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
        etPrice.setText(0);
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
