package com.example.futsalnepal.futsal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.futsalnepal.MainActivity;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class FutsalHome extends AppCompatActivity {


    private BottomNavigationView bNav;
    private FrameLayout bNavFrame;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fDatabase;
    private FutsalBookInfoFragment bookInformation;
    private FutsalBookNowFragment bookNow;
    private FutsalProfile fProfile;
    private FutsalRatingReview fRatingReview;
    private String user_id;
    private TextView txtViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_home);
        bNav = findViewById(R.id.futsal_nav);
        removePaddingFromNavigationItem();

        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();
        bNavFrame = findViewById(R.id.futsal_frame);

        user_id = mAuth.getCurrentUser().getUid();
        bookInformation = new FutsalBookInfoFragment();
        bookNow = new FutsalBookNowFragment();
        fProfile = new FutsalProfile();
        fRatingReview = new FutsalRatingReview();


        setFragment(bookInformation);

        bNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.nav_book_info:
                        setFragment(bookInformation);
                        //setFragment(bookInformation);
                        return true;

                    case  R.id.nav_book_now:
                        setFragment(bookNow);
                        return true;

                    case  R.id.nav_profile:
                        setFragment(fProfile);
                        return true;

                    case R.id.nav_rating_review:
                        setFragment(fRatingReview);
                        return true;

                    default:
                        return false;

                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.futsal_user_menu, menu);
        final View notificaitons = menu.findItem(R.id.notification_btn).getActionView();
        txtViewCount = (TextView) notificaitons.findViewById(R.id.icon_badge);
        setNotificationBadge();
        notificaitons.setOnClickListener(v -> {
            Intent notfyIntent = new Intent(FutsalHome.this, FutsalNotificationActivity.class);
            startActivity(notfyIntent);
        });
        return true;

    }

    private void setNotificationBadge() {
        fDatabase.collection("futsal_list").document(user_id).collection("Notification").whereEqualTo("status", "notseen").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if(snapshot != null){
                    if(snapshot.size() != 0) {
                        txtViewCount.setVisibility(View.VISIBLE);
                        String size = String.valueOf(snapshot.size());
                        txtViewCount.setText(size);

                    }else{
                        txtViewCount.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//
//            case R.id.notification_btn:
//                Intent notfyIntent = new Intent(FutsalHome.this, FutsalNotificationActivity.class);
//                startActivity(notfyIntent);

            case R.id.logout_btn:
                Map<String,Object> tokenMap = new HashMap<>();
                tokenMap.put("token_id", FieldValue.delete());
                fDatabase.collection("futsal_list").document(user_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        Intent signOutIntent = new Intent(FutsalHome.this, MainActivity.class);
                        startActivity(signOutIntent);
                        finish();
                    }
                });
                return true;
            case R.id.setting_btn:
                Intent settingIntent = new Intent(FutsalHome.this, FutsalInfoEdit.class);
                startActivity(settingIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTran = getSupportFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.futsal_frame, fragment);
        fragmentTran.commit();

    }

    public void removePaddingFromNavigationItem() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bNav.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View activeLabel = item.findViewById(R.id.largeLabel);
            if (activeLabel instanceof TextView) {
                activeLabel.setPadding(0, 0, 0, 0);
            }
        }
    }

}
