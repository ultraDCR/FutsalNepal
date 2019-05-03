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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    List<Futsal> futsal_list;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        futsal_list = new ArrayList<>();
        RecyclerView recyclerView =  view.findViewById(R.id.history_rview);
        HistoryRecyclerView adapter = new HistoryRecyclerView(futsal_list, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("user_list").document(user_id).collection("book_info").document("booked")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            Map<String,Object> dd = task.getResult().getData();
                            Log.d("TESTING@",""+dd);
                            for(String user:dd.keySet()){
                                Log.d("TESTING@",""+user);
                                Boolean cmp = compareDate(user);
                                Log.d("DATETEST1",""+cmp);
                                if (cmp) {
                                    Map<String, Object> dd1 = (Map<String, Object>) task.getResult().get(user);
                                    for (String futsalid : dd1.keySet()) {
                                        if (futsalid != null) {
                                            mDatabase.collection("futsal_list").document(futsalid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                    if (documentSnapshot != null) {
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
                }
            });
        }

        return view;
    }
    public Boolean compareDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date date1 = sdf.parse(date);
            String currentDate = sdf.format(new Date());
            Date date2 = sdf.parse(currentDate);
        if(date1.before(date2)){
            Log.e("app", "Date1 is before Date2");
            return true ;
        }
//            if(date1.after(date2)){
//                Log.e("app", "Date1 is after Date2");
//                return true;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
