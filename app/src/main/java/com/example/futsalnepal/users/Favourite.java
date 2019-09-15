package com.example.futsalnepal.users;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;

import com.example.futsalnepal.EmptyRecyclerView;
import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {
    List<Futsal> futsalList;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    String user_id;
    EmptyRecyclerView recyclerView;
    FavouriteRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = findViewById(R.id.favourite_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        futsalList = new ArrayList<>();
        recyclerView = findViewById(R.id.favourite_rview);
        recyclerView.setEmptyView(findViewById(R.id.empty_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(Favourite.this));
        adapter = new FavouriteRecyclerView(futsalList, getApplication());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("users_list").document(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("ERROE", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        ArrayList<String> futsalId = (ArrayList<String>) snapshot.get("favourite_futsal");
                        if(futsalId != null) {
                            futsalList.clear();
                            for (int i = 0; i < futsalId.size(); i++) {
                                String futsal_id = futsalId.get(i);
                                Log.d("TESTING", "onEvent: " + futsal_id + "   ---" + futsalId);
                                mDatabase.collection("futsal_list").document(futsal_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                        if (documentSnapshot != null) {
                                            Log.d("TESTING", "onEvent: " + documentSnapshot);
                                            Futsal futsals = documentSnapshot.toObject(Futsal.class).withId(futsal_id);
                                            Log.d("TESTING1", "onComplete: " + futsals);
                                            futsalList.add(futsals);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }

    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}