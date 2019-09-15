package com.example.futsalnepal.users;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.example.futsalnepal.R;
import com.example.futsalnepal.futsal.BookInformationUserVpagerAdater;

public class BookingInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);
        Toolbar toolbar = findViewById(R.id.book_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //View Pager
        ViewPager viewPager =  findViewById(R.id.book_info_user_vpager);
        BookInformationUserVpagerAdater myPagerAdapter = new BookInformationUserVpagerAdater(this, getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tablayout =  findViewById(R.id.book_info_user_tablayout);
        tablayout.setupWithViewPager(viewPager);

    }

    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
