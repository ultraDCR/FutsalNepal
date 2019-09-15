package com.example.futsalnepal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private TextView uname,uemail,uphone,uaddress,uregister;
    private CircleImageView uprofile, callNow;
    private FirebaseFirestore mDatabase;
    private String user_id;
    private String phoneNO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mDatabase = FirebaseFirestore.getInstance();

        uname = findViewById(R.id.user_name);
        uemail = findViewById(R.id.user_email);
        uphone = findViewById(R.id.user_phone);
        uaddress = findViewById(R.id.user_location);
        uregister = findViewById(R.id.created_date);
        uprofile = findViewById(R.id.user_profile);
        callNow = findViewById(R.id.call_now);

        user_id = getIntent().getStringExtra("user_id");

        mDatabase.collection("users_list").document(user_id).addSnapshotListener((doc, e) -> {

            if(doc != null){
                uname.setText(doc.get("user_full_name").toString());
                uemail.setText(doc.get("user_email").toString());
                phoneNO = doc.get("user_phone_number").toString();
                uphone.setText(phoneNO);
                uaddress.setText(doc.get("user_address").toString());

                Glide.with(this).load(doc.get("user_profile_image").toString()).into(uprofile);

                Timestamp timestamp = (Timestamp) doc.get("created_at");


                long millisecond = timestamp.toDate().getTime();
                String dateString = DateFormat.format("MM dd, yyyy", new Date(millisecond)).toString();
                uregister.setText(dateString);

            }
        });

    }
}
