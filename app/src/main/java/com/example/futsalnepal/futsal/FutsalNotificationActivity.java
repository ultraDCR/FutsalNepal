package com.example.futsalnepal.futsal;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

public class FutsalNotificationActivity extends AppCompatActivity {
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
        adapter = new NotificationRecyclerViewAdapter( "futsal",notificationList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FutsalNotificationActivity.this));

        user_id = mAuth.getCurrentUser().getUid();
        mDatabase.collection("futsal_list").document(user_id).collection("Notification").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        Notifications notification = doc.toObject(Notifications.class).withId(id);
                        notificationList.add(notification);
                        adapter.notifyDataSetChanged();
                        Log.d("NOTIFYTEST3", "onEvent: " + notificationList + "_" + prevDate + "__" + currentDate);
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
