package com.example.futsalnepal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.futsalnepal.Model.Futsal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Favourite extends AppCompatActivity {
    List<Futsal> futsalList ;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    String user_id;
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.favourite_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(Favourite.this));
        FavouriteRecyclerView adapter = new FavouriteRecyclerView(futsalList, getApplication());
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        if(mAuth.getCurrentUser() != null) {
            user_id =mAuth.getCurrentUser().getUid();
            mDatabase.collection("user_list").document(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("ERROE", "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        ArrayList<String> futsalId = (ArrayList<String>) snapshot.get("favourite_futsal");
                        futsalList.clear();
                        for(int i=0; i< futsalId.size();i++) {
                            String futsal_id = futsalId.get(i);
                            Log.d("TESTING", "onEvent: "+futsal_id +"   ---"+futsalId);
                            mDatabase.collection("futsal_list").document(futsal_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                    if(documentSnapshot != null) {
                                        Log.d("TESTING", "onEvent: " + documentSnapshot);
                                        Futsal futsals = documentSnapshot.toObject(Futsal.class).withId(futsal_id);
                                        Log.d("TESTING1", "onComplete: "+futsals);
                                        futsalList.add(futsals);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
// addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                @Override
//                                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                                    if(!documentSnapshot.exists()) {
//                                        Log.d("TESTING", "onEvent: "+documentSnapshot);
//                                        Futsal futsals = documentSnapshot.toObject(Futsal.class).withId(futsal_id);
//                                        futsalList.add(futsals);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                }
//                            });

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

//    public List<Futsal> fill_with_data() {
//
//        List<Futsal> data = new ArrayList<>();
//
//        data.add(new Futsal("WhiteHouse", "Kapan-3","6AM","6PM", "9796875685",4));
//
//        return data;
//    }
}
