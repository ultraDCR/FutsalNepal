package com.example.futsalnepal;

import android.app.Dialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.balysv.materialmenu.MaterialMenu;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.balysv.materialmenu.MaterialMenuDrawable.IconState.BURGER;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mainToolbar;

    private SliderLayout sliderShow;

    private Button searchBtn;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView = null;

    private String[] images = {
            "https://5.imimg.com/data5/JJ/PX/MY-5974440/futsal-ground-artificial-grass-500x500.jpg",
            "https://ranknepal.com/wp-content/uploads/2014/06/footsal-ground-inside-kathmandu-valley.jpg",
            "https://5.imimg.com/data5/OE/GB/MY-2392315/futsal-artificial-grass-ground-500x500.jpg"

    };
    List<Data> data = fill_with_data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        mainToolbar.setTitle("Futsal Nepal");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Nav Bar
        drawerLayout =  findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_bar);
        navigationView.setNavigationItemSelectedListener(this);

        //image slider
        sliderShow =  findViewById(R.id.slider);
        for(int position =0 ; position < images.length ; position ++ ){
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView
                    .image(images[position])
                    //.setScaleType(BaseSliderView.ScaleType.Fit)
            ;

            sliderShow.addSlider(sliderView);
        }

        sliderShow.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(9000);

        searchBtn = findViewById(R.id.home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this, SearchLayout.class);
                startActivity(searchIntent);
            }
        });


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.top_rated_futsal);
        FutsalRecycleView adapter = new FutsalRecycleView(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));



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

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialog));

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.login_signup_dialog, null);
                builder.setView(dialogView);

                LoginSignupFragmentPagerAdapter adapter = new LoginSignupFragmentPagerAdapter(MainActivity.this);
                ViewPager viewPager = dialogView.findViewById(R.id.login_signup_view);
                TabLayout tabLayout =  dialogView.findViewById(R.id.login_sign_maintab);
                tabLayout.setupWithViewPager(viewPager);
                viewPager.setAdapter(adapter);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                dialog.getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);




                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {


        switch(item.getItemId()){
            case R.id.book_info_menu:
                Intent bookInfoIntent = new Intent(MainActivity.this, BookingInformation.class);
                startActivity(bookInfoIntent);
            case R.id.find_arena_menu:
                Intent findArenaIntent = new Intent(MainActivity.this, SearchLayout.class);
                startActivity(findArenaIntent);

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("WhiteHouse", "Kapan-3","6AM - 6PM", R.mipmap.ic_futsal_foreground,4));
        data.add(new Data("BlackHouses", "Chabahil","9AM - 9PM", R.drawable.logo,2));

        return data;
    }

}
