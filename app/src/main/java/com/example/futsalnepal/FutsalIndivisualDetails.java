package com.example.futsalnepal;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class FutsalIndivisualDetails extends AppCompatActivity {

    private TextView fPhone, fAddress;
    private CircleImageView fLogo;
    private String futsal_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_indivisual_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout =findViewById(R.id.htab_collapse_toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        fPhone = findViewById(R.id.tab_phone_no);
        fAddress = findViewById(R.id.tab_address);
        fLogo = findViewById(R.id.futsal_logo);

        futsal_id = getIntent().getStringExtra("futsal_id");

        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String futsal_name = task.getResult().getString("futsal_name");
                    String futsal_address = task.getResult().getString("futsal_address");
                    String futsal_phone = task.getResult().getString("futsal_phone");
                    String futsal_logo = task.getResult().getString("futsal_logo");

                    collapsingToolbarLayout.setTitle(futsal_name);
                    fPhone.setText(futsal_phone);
                    fAddress.setText(futsal_address);
                    RequestOptions placeholderRequest = new RequestOptions();
                    placeholderRequest.placeholder(R.drawable.logo_placeholder_circle);

                    Glide.with(FutsalIndivisualDetails.this).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(fLogo);

                }
            }
        });


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

    public String getMyData() {
        return futsal_id;
    }
    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
