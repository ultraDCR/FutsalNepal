package com.example.futsalnepal;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FutsalHome extends AppCompatActivity {


    private BottomNavigationView bNav;
    private FrameLayout bNavFrame;

    private FutsalBookInfoFragment bookInformation;
    private FutsalBookNowFragment bookNow;
    private FutsalProfile fProfile;
    private FutsalRatingReview fRatingReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_home);
        bNav = findViewById(R.id.futsal_nav);
        removePaddingFromNavigationItem();


        bNavFrame = findViewById(R.id.futsal_frame);

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
