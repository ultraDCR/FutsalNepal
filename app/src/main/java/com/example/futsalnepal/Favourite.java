package com.example.futsalnepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {
    List<Data> data = fill_with_data();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = findViewById(R.id.favourite_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favourite_rview);
        FutsalRecycleView adapter = new FutsalRecycleView(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Favourite.this));
    }
    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("WhiteHouse", "Kapan-3","6AM - 6PM", R.mipmap.ic_futsal_foreground,4));
        data.add(new Data("BlackHouses", "Chabahil","9AM - 9PM", R.drawable.ic_profile,2));

        return data;
    }
}
