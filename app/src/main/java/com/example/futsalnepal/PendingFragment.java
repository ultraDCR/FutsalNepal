package com.example.futsalnepal;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.futsalnepal.Model.Data;
import com.example.futsalnepal.Model.Futsal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {

    List<Futsal> futsal_list;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;
    public PendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        futsal_list = new ArrayList<>();
        RecyclerView recyclerView =  view.findViewById(R.id.pending_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        PendingRequestRecyclerView adapter = new PendingRequestRecyclerView(futsal_list, getContext());
        recyclerView.setAdapter(adapter);
        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("user_list").document(user_id).collection("book_info").document("pending")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            Map<String,Object> dd = task.getResult().getData();
                            Log.d("TESTING@",""+dd);
                            for(String user:dd.keySet()){
                                Log.d("TESTING@",""+user);
                                Map<String,Object> dd1 = (Map<String, Object>) task.getResult().get(user);
                                for (String futsalid: dd1.keySet()){
                                    if(futsalid != null){
                                        mDatabase.collection("futsal_list").document(futsalid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if(documentSnapshot !=null){
                                                    Futsal futsal = documentSnapshot.toObject(Futsal.class).withId(futsalid);
                                                    futsal_list.add(futsal);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }
                                }

                            }
                        }
                    }
                }
            });
        }

        return view;
    }



}
