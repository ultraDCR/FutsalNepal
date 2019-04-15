package com.example.futsalnepal;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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
