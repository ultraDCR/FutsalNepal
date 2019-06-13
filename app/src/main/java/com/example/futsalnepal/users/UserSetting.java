package com.example.futsalnepal.users;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.futsalnepal.MainActivity;
import com.example.futsalnepal.R;

public class UserSetting extends AppCompatActivity {

    private Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        editProfile = findViewById(R.id.edit_profile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(UserSetting.this, UserInfoEdit.class);
                startActivity(settingIntent);
            }
        });
    }
}
