package com.example.futsalnepal;

import android.app.Notification;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.Model.Notifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private List<Futsal> futsalList;
    private List<Notifications> notificationList;
    private NotificationRecyclerViewAdapter adapter;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notification);

        Toolbar mainToolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(mainToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        //String dataMessage = getIntent().getStringExtra("message");

        //futsalList = new ArrayList<>();
        notificationList = new ArrayList<>();
        recyclerView = findViewById(R.id.notification_rview);
        adapter = new NotificationRecyclerViewAdapter( notificationList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNotificationActivity.this));

        user_id = mAuth.getCurrentUser().getUid();
        mDatabase.collection("user_list").document(user_id).collection("Notification").orderBy("timestamp", Query.Direction.DESCENDING).limit(15).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null){
                    //futsalList.clear();
                    notificationList.clear();
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        String id = doc.getId();
                        Notifications notification = doc.toObject(Notifications.class).withId(id);
                        notificationList.add(notification);
                        adapter.notifyDataSetChanged();
                        Log.d("NOTIFYTEST", "onEvent: "+notificationList);
                    }
                }
            }
        });



    }
    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}