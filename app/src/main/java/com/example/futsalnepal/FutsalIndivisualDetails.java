package com.example.futsalnepal;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class FutsalIndivisualDetails extends AppCompatActivity {

    private TextView fPhone, fAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_indivisual_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout =(CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);


        collapsingToolbarLayout.setTitle("HamroFutsal");
//        collapsingToolbarLayout.setContentScrimColor(Color.GREEN);

        fPhone = findViewById(R.id.tab_phone_no);
        fPhone.setText("9867584948");

        fAddress = findViewById(R.id.tab_address);
        fAddress.setText("Kapan-3, KTM");

        ImageView fFavourite = findViewById(R.id.favourite_btn);
        fFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fFavourite.setImageResource(R.drawable.ic_favorite_selected);
            }
        });

        //View Pager
        ViewPager viewPager =  findViewById(R.id.futsal_viewpager);
        FutsalDetailViewPageAdapter myPagerAdapter = new FutsalDetailViewPageAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tablayout =  findViewById(R.id.futsal_tablayout);
        tablayout.setupWithViewPager(viewPager);


    }


}
