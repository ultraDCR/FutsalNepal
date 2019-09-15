package com.example.futsalnepal.users;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.MainActivity;
import com.example.futsalnepal.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserSetting extends AppCompatActivity {

    private Button editProfile,logout;
    private TextView user_name, user_email;
    private ImageView user_profile;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        user_name = findViewById(R.id.setting_username);
        user_email = findViewById(R.id.setting_email);
        user_profile = findViewById(R.id.setting_user_profile);
        editProfile = findViewById(R.id.edit_profile);
        logout = findViewById(R.id.logout_setting);

        editProfile.setOnClickListener(v -> {
            Intent settingIntent = new Intent(UserSetting.this, UserInfoEdit.class);
            startActivity(settingIntent);
        });

        logout.setOnClickListener( v -> {
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            startActivity(new Intent(UserSetting.this,MainActivity.class));
            finish();
        });

        String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.collection("users_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        if (task.getResult().getString("user_full_name") != null) {
                            String name = task.getResult().getString("user_full_name");
                            String image = task.getResult().getString("user_profile_image");
                            String email = mAuth.getCurrentUser().getEmail();

                            user_name.setText(name);
                            user_email.setText(email);
                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile_image);

                            Glide.with(UserSetting.this).setDefaultRequestOptions(placeholderRequest).load(image).into(user_profile);

                        }
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(UserSetting.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
