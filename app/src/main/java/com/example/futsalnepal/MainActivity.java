package com.example.futsalnepal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mainToolbar;

    private SliderLayout sliderShow;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView = null;

    private String[] images = {
            "https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg",
            "https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg",
            "https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png"

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Futsal Nepal");

        //image slider
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        for(int position =0 ; position < images.length ; position ++ ){
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView
                    .image(images[position])
                    //.setScaleType(BaseSliderView.ScaleType.Fit)
            ;

            sliderShow.addSlider(sliderView);
        }


        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(4000);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState();

        navigationView = findViewById(R.id.nav_bar);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_login_btn:

//                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
//                startActivity(settingsIntent);

                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {


        switch(item.getItemId()){


        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
