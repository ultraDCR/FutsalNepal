package com.example.futsalnepal;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FutsalIndivisualDetails extends AppCompatActivity {

    private TextView fPhone, fAddress;
    private CircleImageView fLogo;
    private ImageView favBtn;
    private String futsal_id;
    private String user_id;
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
        favBtn = findViewById(R.id.favourite_btn);

//        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
//        fLogo.startAnimation(hyperspaceJump);

        futsal_id = getIntent().getStringExtra("futsal_id");

        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String futsal_name = task.getResult().getString("futsal_name");

                    String futsal_phone = task.getResult().getString("futsal_phone");
                    String futsal_logo = task.getResult().getString("futsal_logo");

                    Map<String, Object> location = (Map<String, Object>) task.getResult().get("location");
                    String futsal_address =  location.get("vdc")+", "+location.get("district");

                    collapsingToolbarLayout.setTitle(futsal_name);
                    fPhone.setText(futsal_phone);
                    fAddress.setText(futsal_address);
                    RequestOptions placeholderRequest = new RequestOptions();
                    placeholderRequest.placeholder(R.drawable.logo_placeholder_circle);

                    Glide.with(FutsalIndivisualDetails.this).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(fLogo);

                }
            }
        });

        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alertBoxFavourite();
                    //favBtn.setImageResource(R.drawable.ic_favorite_selected);
                }
            });
            mDatabase.collection("users_list").document(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("ERROR", "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        if(snapshot.get("favourite_futsal") != null) {
                            Log.d("FAVIOU", "onComplete: " +snapshot.get("favourite_futsal") );

                            ArrayList<String> futsals = (ArrayList<String>) snapshot.get("favourite_futsal");
                            Log.d("FAVIOU", "onComplete: " + futsals);
                            if(futsals.contains(futsal_id)){
                                favBtn.setImageResource(R.drawable.ic_favorite_selected);
                                favBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertBoxUnFavourite();
//                                        mDatabase.collection("users_list").document(user_id).update("favourite_futsal", FieldValue.arrayRemove(futsal_id));
                                    }
                                });
                            }
                            else{
                                favBtn.setImageResource(R.drawable.ic_favourite_unselected_white);

                                favBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertBoxFavourite();
                                    }
                                });
                            }
                        }
                    } else {
                        Log.d("RETRIVE ERROR", "Current data: null");

                    }
                }
            });
        }else{
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new LoginAndSignUp();
                    newFragment.show(getSupportFragmentManager(), "favioutar");
                    Log.d("pressed", "alertdialog");
                }
            });
        }

        //View Pager
        ViewPager viewPager =  findViewById(R.id.futsal_viewpager);
        FutsalDetailViewPageAdapter myPagerAdapter = new FutsalDetailViewPageAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tablayout =  findViewById(R.id.futsal_tablayout);
        tablayout.setupWithViewPager(viewPager);


    }

    private void alertBoxFavourite() {
        new AlertDialog.Builder(FutsalIndivisualDetails.this)
                .setMessage("Do you want to favourite this futsal? ")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ALERTTEST", "onClick: YES" + dialog + "  " + which);
                        Log.d("ALERTTEST", "onClick: YES" + user_id + "  " + futsal_id);
                        mDatabase.collection("users_list").document(user_id).update("favourite_futsal", FieldValue.arrayUnion(futsal_id));
//                        Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on Yes"+user_id+"   "+futsal_id, Toast.LENGTH_SHORT).show();
                        favBtn.setImageResource(R.drawable.ic_favorite_selected);

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    private void alertBoxUnFavourite() {
            new AlertDialog.Builder(FutsalIndivisualDetails.this)
                    .setMessage("Are you sure you want to unfavourite this futsal?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Log.d("ALERTTEST", "onClick: YES"+dialog+"  "+which);
                            Log.d("ALERTTEST", "onClick: YES"+user_id+"  "+futsal_id);
                            mDatabase.collection("users_list").document(user_id).update("favourite_futsal", FieldValue.arrayRemove(futsal_id));
                            //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on Yes"+user_id+"   "+futsal_id, Toast.LENGTH_SHORT).show();

                            favBtn.setImageResource(R.drawable.ic_favorite_selected);


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
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
