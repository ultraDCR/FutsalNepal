package com.example.futsalnepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.Model.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Futsal> futsalList;
    private List<Notifications> notificationList;
    private NotificationRecyclerViewAdapter adapter;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        //String dataMessage = getIntent().getStringExtra("message");

        recyclerView = findViewById(R.id.notification_rview);
        adapter = new NotificationRecyclerViewAdapter(futsalList, notificationList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNotificationActivity.this));




    }
}
