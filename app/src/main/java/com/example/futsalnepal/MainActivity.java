package com.example.futsalnepal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.futsal.FutsalHome;
import com.example.futsalnepal.users.BookingInformation;
import com.example.futsalnepal.users.Favourite;
import com.example.futsalnepal.users.UserInfoEdit;
import com.example.futsalnepal.users.UserNotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mainToolbar;

    private SliderLayout sliderShow;

    private Button searchBtn;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView = null;
    private CircleImageView dUserPic;
    private TextView dUserName, dUserAddress, dUserPhone, badge;
    private ImageView settingBtn, logOutBtn;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private StorageReference mStorage;
    private String user_id;
    private Uri mainImageURI = null;
    private String[] images = {
            "https://5.imimg.com/data5/JJ/PX/MY-5974440/futsal-ground-artificial-grass-500x500.jpg",
            "https://5.imimg.com/data5/OE/GB/MY-2392315/futsal-artificial-grass-ground-500x500.jpg"

    };
    List<Futsal> futsalList ;
    private Boolean haveInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        //toolbar
        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Nav Bar
        drawerLayout =  findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_bar);

        navigationView.setNavigationItemSelectedListener(this);

        //nav bar header elements
        View header = navigationView.getHeaderView(0);
        dUserName = header.findViewById(R.id.user_name);
        dUserAddress = header.findViewById(R.id.user_address);
        dUserPhone = header.findViewById(R.id.user_number);
        dUserPic = header.findViewById(R.id.user_image);

        settingBtn = header.findViewById(R.id.setting_btn);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(MainActivity.this, UserInfoEdit.class);
                startActivity(settingIntent);
            }
        });
        logOutBtn = header.findViewById(R.id.logout_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mAuth.getCurrentUser().getUid();
                Map<String,Object> tokenMap = new HashMap<>();
                tokenMap.put("token_id", FieldValue.delete());
                mDatabase.collection("users_list").document(uid).set(tokenMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        Intent signOutIntent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(signOutIntent);
                        finish();
                    }
                });

            }
        });

        Menu nav_Menu = navigationView.getMenu();

       // getMenuInflater().inflate(R.menu.drawer_menu, nav_Menu);
        ConstraintLayout notify = (ConstraintLayout) nav_Menu.findItem(R.id.notification_menu).getActionView();
        badge = (TextView) notify.findViewById(R.id.noicon_badge);
        notify.setVisibility(View.INVISIBLE);

        //hide nav bar item on login
        if(mAuth.getCurrentUser() != null){

            nav_Menu.findItem(R.id.book_info_menu).setVisible(true);
            nav_Menu.findItem(R.id.favourite_menu).setVisible(true);
            nav_Menu.findItem(R.id.notification_menu).setVisible(true);

            logOutBtn.setVisibility(View.VISIBLE);
        }else{
            nav_Menu.findItem(R.id.book_info_menu).setVisible(false);
            nav_Menu.findItem(R.id.favourite_menu).setVisible(false);
            nav_Menu.findItem(R.id.notification_menu).setVisible(false);

            logOutBtn.setVisibility(View.INVISIBLE);
        }




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

        //search
        searchBtn = findViewById(R.id.home_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(MainActivity.this, SearchLayout.class);
                startActivity(searchIntent);
            }
        });

        //top rated futsal
        futsalList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.top_rated_futsal);
        FutsalRecycleView adapter = new FutsalRecycleView(futsalList, getApplication());
        recyclerView.setAdapter(adapter);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mDatabase.collection("futsal_list").orderBy("overall_rating", Query.Direction.DESCENDING).limit(3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    futsalList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String futsalId = doc.getId();
                        Log.d("TESTING", "onEvent: " + doc);
                        Futsal futsals = doc.toObject(Futsal.class).withId(futsalId);
                        futsalList.add(futsals);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });


        if(mAuth.getCurrentUser() != null) {

            //data for drawer header elements
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("users_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {

                        if (task.getResult().exists()) {
                            String name = task.getResult().getString("user_full_name");
                            String image = task.getResult().getString("user_profile_image");
                            String address = task.getResult().getString("user_address");
                            String phone = task.getResult().getString("user_phone_number");

                            //mainImageURI = Uri.parse(image);
                            dUserName.setVisibility(View.VISIBLE);
                            dUserAddress.setVisibility(View.VISIBLE);
                            dUserPhone.setVisibility(View.VISIBLE);
                            dUserName.setText(name);
                            dUserAddress.setText(address);
                            dUserPhone.setText(phone);


                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile_image);

                            Glide.with(MainActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(dUserPic);


                        }

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                    }
                }
            });


            //for notification badge
            mDatabase.collection("users_list").document(user_id).collection("Notification").whereEqualTo("status", "notseen").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if(snapshot != null){
                        if(snapshot.size() != 0) {
                            actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
                            notify.setVisibility(View.VISIBLE);
                            String size = String.valueOf(snapshot.size());
                            initializeCountDrawer(size);

                        }else{
                            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
                            notify.setVisibility(View.INVISIBLE);
                        }

                    }
                }
            });
         }

         //Push notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("FutsalTime","FutsalTime", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("TestingInfo1", "onComplete: "+ currentUser);

        if(currentUser != null) {

            user_id = mAuth.getCurrentUser().getUid();

            mDatabase.collection("futsal_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("TestingInfo2", "onComplete: "+ currentUser);
                        if (task.getResult().exists()) {
                            //redirect to futsal page
                            Intent futsalhome = new Intent(MainActivity.this, FutsalHome.class);
                            startActivity(futsalhome);
                            finish();
                        } else {
                            Log.d("TestingInfo3", "onComplete: "+ mDatabase.collection("users_list").document(user_id).get());
                            mDatabase.collection("users_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Log.d("TestingInfo4", "onComplete: "+task);
                                    if (task.isSuccessful()) {
                                        Log.d("TestingInfo4", "onComplete: "+ task.getResult().exists());
                                        if (task.getResult().exists()) {
                                            Log.d("TestingInfo", "onComplete: " + task.getResult().getString("user_full_name"));
                                            if (task.getResult().getString("user_full_name") == null) {
                                                Intent user_info_edit = new Intent(MainActivity.this, UserInfoEdit.class);
                                                startActivity(user_info_edit);
                                                finish();
                                            }

                                        }
                                    }
                                }
                            });

                        }
                    }
                }
            });

        }
    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        //hide login menu if not login
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            MenuItem item = menu.findItem(R.id.action_login_btn);
            item.setVisible(false);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_login_btn:
                LoginDialog dialog = new LoginDialog(MainActivity.this,this);
                dialog.startLoginDialog();
//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialog));
//
//                LayoutInflater inflater = this.getLayoutInflater();
//                View dialogView = inflater.inflate(R.layout.login_signup_dialog, null);
//                builder.setView(dialogView);
//
//                LoginSignupFragmentPagerAdapter adapter = new LoginSignupFragmentPagerAdapter(MainActivity.this);
//                ViewPager viewPager = dialogView.findViewById(R.id.login_signup_view);
//                TabLayout tabLayout =  dialogView.findViewById(R.id.login_sign_maintab);
//                tabLayout.setupWithViewPager(viewPager);
//                viewPager.setAdapter(adapter);
//                AlertDialog dialog = builder.create();
//                dialog.show();
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//                dialog.getWindow().clearFlags(
//                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

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
                return true;

            case R.id.find_arena_menu:
                Intent findArenaIntent = new Intent(MainActivity.this, SearchLayout.class);
                startActivity(findArenaIntent);
                return true;

            case R.id.notification_menu:
                Intent notificationIntent = new Intent(MainActivity.this, UserNotificationActivity.class);
                startActivity(notificationIntent);
                return true;

            case R.id.favourite_menu:
                Intent favouriteIntent = new Intent(MainActivity.this, Favourite.class);
                startActivity(favouriteIntent);
                return true;

            case R.id.about_us_menu:

                return true;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void initializeCountDrawer(String size){
        //Gravity property aligns the text
//        badge.setGravity(Gravity.CENTER_VERTICAL);
//        badge.setTypeface(null,Typeface.BOLD);
//        badge.setTextColor(getResources().getColor(R.color.red));
//        badge.setBackgroundResource(R.drawable.badge_background);
//        badge.setHeight(20);
//        badge.setWidth(20);
        //count is added

        badge.setText(size);
    }


//    public List<Data> fill_with_data() {
//
//        List<Data> data = new ArrayList<>();
//
//        data.add(new Data("WhiteHouse", "Kapan-3","6AM - 6PM", R.mipmap.ic_futsal_foreground,4));
//        data.add(new Data("BlackHouses", "Chabahil","9AM - 9PM", R.drawable.logo,2));
//
//        return data;
//    }

}
