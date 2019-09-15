package com.example.futsalnepal.users;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;


import com.example.futsalnepal.Model.NotificationSectionModel;
import com.example.futsalnepal.Model.Notifications;
import com.example.futsalnepal.NotificationRecyclerViewAdapter;
import com.example.futsalnepal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class UserNotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private List<Futsal> futsalList;
    private List<Notifications> notificationList;
    private List<NotificationSectionModel> sectionList;
    //private NotificationDateSectionAdapter adapter;
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
        sectionList = new ArrayList<>();
        recyclerView = findViewById(R.id.notification_rview);
        //adapter = new NotificationDateSectionAdapter( sectionList, getApplication());
        adapter = new NotificationRecyclerViewAdapter( "user",notificationList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserNotificationActivity.this));

        user_id = mAuth.getCurrentUser().getUid();
        mDatabase.collection("users_list").document(user_id).collection("Notification").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots != null){
                    //futsalList.clear();
                    notificationList.clear();
                    String prevDate = null;
                    String currentDate = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        String id = doc.getId();
//                        Timestamp time2 = (Timestamp) doc.get("timestamp");
//                        Date cdate = time2.toDate();
//                        Log.d("NOTIFYTEST2", "onEvent: "+cdate);
//                        if(prevDate == null){
//                            prevDate = sdf.format(cdate);
//                        }
//                        currentDate = sdf.format(cdate);
//                        Log.d("NOTIFYTEST1", "onEvent: "+time2+"_"+prevDate+"__"+currentDate);
//                       if(prevDate.equals(currentDate)) {
                           Notifications notification = doc.toObject(Notifications.class).withId(id);
                           notificationList.add(notification);

//                           sectionList.add(new NotificationSectionModel(currentDate, notificationList));
                           adapter.notifyDataSetChanged();
                           Log.d("NOTIFYTEST3", "onEvent: " + notificationList + "_" + prevDate + "__" + currentDate);
//                       }else{
//                           prevDate = currentDate;
//                       }
                        //notificationList.clear();

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
