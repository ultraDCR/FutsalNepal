package com.example.futsalnepal;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookingFutsal;
import com.example.futsalnepal.Model.SectionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    List<SectionModel> sectionModelArrayList;
    List<BookingFutsal> futsal_list;
    List<BookingFutsal> h_list;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;
    private String date,yestardayDate;
    DatePickerDialog dpd;
    private TextView fDatePicker;
    private RecyclerView recyclerView;
    private DateSectionFutsalRecyclerViewAdapter sadapter;


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


        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        Calendar current = Calendar.getInstance();
        current.add(Calendar.DATE,-1);
        date = sdf.format(current.getTime());
        yestardayDate = sdf.format(current.getTime());
        Log.d("TIMETEST", "onCreateView: "+date);

        fDatePicker = view.findViewById(R.id.date_picker_history);
        fDatePicker.setText(date);
        Switch fSwitch = view.findViewById(R.id.history_switch);
        ConstraintLayout datepickLayout = view.findViewById(R.id.date_filter_history);
        Boolean switchState = fSwitch.isChecked();

        sectionModelArrayList = new ArrayList<>();
        recyclerView =  view.findViewById(R.id.history_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        sadapter = new DateSectionFutsalRecyclerViewAdapter("history",this.getContext(), sectionModelArrayList);
        Log.d("DATETEST9",""+sadapter);
        recyclerView.setAdapter(sadapter);
        //PendingRequestRecyclerView adapter = new PendingRequestRecyclerView(futsal_list, getContext());
        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            loadFutsalcalss();
            fSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        datepickLayout.setVisibility(View.INVISIBLE);
                        sectionModelArrayList.clear();
                        loadToRecyclerViewWhenOn(sadapter);
                    }else{
                        datepickLayout.setVisibility(View.VISIBLE);
                        sectionModelArrayList.clear();
                        loadToRecyclerViewWhenOff(sadapter);
                    }
                }
            });
        }



        fDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int day = now.get(Calendar.YEAR); // Initial year selection
                int month = now.get(Calendar.MONTH); // Initial month selection
                int year = now.get(Calendar.DAY_OF_MONTH);// Inital day selection


                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDayOfMonth) {
                        Log.d("DATEPICKER", "onDateSet: "+datePicker);
                        now.set(Calendar.YEAR, mYear);
                        now.set(Calendar.MONTH, mMonth);
                        now.set(Calendar.DAY_OF_MONTH, mDayOfMonth);

                        //date = DateFormat.getDateInstance().format(now.getTime());
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        date = sdf.format(now.getTime());
                        fDatePicker.setText(date);
                        sectionModelArrayList.clear();
                        loadToRecyclerViewWhenOff(sadapter);

                    }


                },day,month,year);
                dpd.getDatePicker().setMaxDate(current.getTimeInMillis());
                dpd.show();

            }


        });


        return view;
    }

    private void loadToRecyclerViewWhenOn(DateSectionFutsalRecyclerViewAdapter sadapter){
        mDatabase.collection("users_list").document(user_id).collection("booked")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (snapshot != null) {
                            sectionModelArrayList.clear();
                            for (QueryDocumentSnapshot document : snapshot) {
                                String pdate = document.getId();
                                Log.d("NEWREQTEST2.0.0", "onComplete: " + pdate + "-" + document.getData().get(pdate) + "_" + compareDate(pdate, date));
                                if (compareDate(pdate, yestardayDate)) {
                                    Map<String, Object> dd1 = (Map<String, Object>) document.getData();
                                    h_list = new ArrayList<>();
                                    h_list.clear();
                                    for (String uid : dd1.keySet()) {
                                        for (int i = 0; i < futsal_list.size(); i++) {
                                            Log.d("NEWREQTEST2.2", "onComplete: " + futsal_list.size() + " -- " + futsal_list.get(i).futsal_id + " -- " + uid);
                                            if (futsal_list.get(i).futsal_id.equals(uid)) {
                                                Log.d("NEWREQTEST2.3", "onComplete: " + dd1.get(uid));
                                                Map<String, Object> dd2 = (Map<String, Object>) dd1.get(uid);
                                                for (String time : dd2.keySet()) {

                                                    BookingFutsal futsal1 = new BookingFutsal();
                                                    Log.d("FIREBASETEST2.3", "onComplete: " + time);
                                                    futsal1.setTime(time);
                                                    futsal1.setFutsal_name(futsal_list.get(i).getFutsal_name());
                                                    futsal1.setFutsal_id(futsal_list.get(i).getFutsal_id());
                                                    futsal1.setFutsal_address(futsal_list.get(i).getFutsal_address());
                                                    futsal1.setFutsal_phone(futsal_list.get(i).getFutsal_phone());
                                                    futsal1.setFutsal_logo(futsal_list.get(i).getFutsal_logo());
                                                    futsal1.setOverall_rating(futsal_list.get(i).getOverall_rating());
                                                    Log.d("FIREBASETEST2.4", "onComplete1: " + futsal1.getTime());
                                                    h_list.add(futsal1);

                                                }

                                                Log.d("FIREBASETEST2.1", "onComplete: " + h_list);
                                            }

                                        }
                                    }
                                    if (h_list.size() != 0) {
                                        sectionModelArrayList.add(new SectionModel(pdate, h_list, null));
                                        sadapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Log.d("ERROR IN RETRIVAL", "Error getting documents: ", e);
                                }
                            }
                            sadapter.notifyDataSetChanged();

                        }
                    }

                });

    }


    private void loadToRecyclerViewWhenOff(DateSectionFutsalRecyclerViewAdapter sadapter){
        mDatabase.collection("users_list").document(user_id).collection("booked")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (snapshot != null) {
                            sectionModelArrayList.clear();
                            for (QueryDocumentSnapshot document : snapshot) {
                                String pdate = document.getId();
                                Log.d("NEWREQTEST2.0.0", "onComplete: " + pdate + "-" + document.getData().get(pdate) + "_" + compareDate(pdate, date));
                                if (pdate.equals(date)) {
                                    Map<String, Object> dd1 = (Map<String, Object>) document.getData();
                                    h_list = new ArrayList<>();
                                    h_list.clear();
                                    for (String uid : dd1.keySet()) {
                                        for (int i = 0; i < futsal_list.size(); i++) {
                                            Log.d("NEWREQTEST2.2", "onComplete: " + futsal_list.size() + " -- " + futsal_list.get(i).futsal_id + " -- " + uid);
                                            if (futsal_list.get(i).futsal_id.equals(uid)) {
                                                Log.d("NEWREQTEST2.3", "onComplete: " + dd1.get(uid));
                                                Map<String, Object> dd2 = (Map<String, Object>) dd1.get(uid);
                                                for (String time : dd2.keySet()) {

                                                    BookingFutsal futsal1 = new BookingFutsal();
                                                    Log.d("FIREBASETEST2.3", "onComplete: " + time);
                                                    futsal1.setTime(time);
                                                    futsal1.setFutsal_name(futsal_list.get(i).getFutsal_name());
                                                    futsal1.setFutsal_id(futsal_list.get(i).getFutsal_id());
                                                    futsal1.setFutsal_address(futsal_list.get(i).getFutsal_address());
                                                    futsal1.setFutsal_phone(futsal_list.get(i).getFutsal_phone());
                                                    futsal1.setFutsal_logo(futsal_list.get(i).getFutsal_logo());
                                                    futsal1.setOverall_rating(futsal_list.get(i).getOverall_rating());
                                                    Log.d("FIREBASETEST2.4", "onComplete1: " + futsal1.getTime());
                                                    h_list.add(futsal1);

                                                }

                                                Log.d("FIREBASETEST2.1", "onComplete: " + h_list);
                                            }

                                        }
                                    }
                                    if (h_list.size() != 0) {
                                        sectionModelArrayList.add(new SectionModel(pdate, h_list, null));
                                        sadapter.notifyDataSetChanged();
                                    }

                                } else {
                                    Log.d("ERROR IN RETRIVAL", "Error getting documents: ", e);
                                }
                            }
                            sadapter.notifyDataSetChanged();

                        }
                    }

                });

    }

    private void loadFutsalcalss() {
        futsal_list = new ArrayList<>();
        mDatabase.collection("futsal_list").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                    String futsalid = documentSnapshot.getId();
                    BookingFutsal futsal = documentSnapshot.toObject(BookingFutsal.class);
                    futsal.setFutsal_id(futsalid);
                    Log.d("DATETESTH0.1", "" + futsal);
                    futsal_list.add(futsal);
                    Log.d("NEWTESTH0.12", "onComplete: "  + futsal_list.size());

                }
                //loadDataToRecyclerView(sadapter);
                loadToRecyclerViewWhenOff(sadapter);
            }
        });
    }


    public Boolean compareDate(String pdate,String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date date1 = sdf.parse(pdate);
            Date date2 = sdf.parse(date);

            if(date1.equals(date2) || date1.before(date2)){
                Log.e("app", "Date1 is before Date2");
                return true ;
            }
//            if(date1.equals(date2) || date1.after(date2)){
//                Log.e("app", "Date1 is after Date2");
//                return true;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
