package com.example.futsalnepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.futsalnepal.Model.BookedUser;

import java.util.ArrayList;
import java.util.List;

public class BookingInfoFutsal extends AppCompatActivity {
    List<BookedUser> booked_user = fill_with_data();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info_futsal);
        Toolbar toolbar = findViewById(R.id.book_info_futsal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.rview_book_info_futsal);
        BookedUserRecyclerView adapter = new BookedUserRecyclerView(booked_user, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookingInfoFutsal.this));
    }
    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<BookedUser> fill_with_data() {

        List<BookedUser> booked_user = new ArrayList<>();


        booked_user.add(new BookedUser("Ranjan Parajuli", "6AM - 10PM","20/10/2019","9816474873"));
        booked_user.add(new BookedUser(" Deependra Dhakal","6AM - 10PM", "22/11/2019","9816474873"));
        booked_user.add(new BookedUser(" Krishna Singh", "6AM - 10PM","22/11/2019","9816474873"));
        booked_user.add(new BookedUser(" Pradeep Shrestha","6AM - 10PM", "22/11/2019","9816474873"));

        return booked_user;
    }
}
