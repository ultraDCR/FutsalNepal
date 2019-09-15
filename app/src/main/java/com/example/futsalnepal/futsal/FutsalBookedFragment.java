package com.example.futsalnepal.futsal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookingUser;
import com.example.futsalnepal.Model.SectionModel;
import com.example.futsalnepal.R;
import com.example.futsalnepal.users.DateSectionUserRecyclerViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class FutsalBookedFragment extends Fragment {

    List<SectionModel> sectionModelArrayList;
    List<BookingUser> users_list;
    List<BookingUser> r_list;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String futsal_id;
    private String date, currentDate;
    DatePickerDialog dpd;
    private Switch fSwitch;
    private Boolean switchState;
    private TextView fDatePicker;
    private RecyclerView recyclerView;
    private DateSectionUserRecyclerViewAdapter sadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        date = sdf.format(new Date());
        currentDate = sdf.format(new Date());

        fDatePicker = view.findViewById(R.id.date_picker_booked);
        fDatePicker.setText(date);
        fSwitch = view.findViewById(R.id.booked_switch);
        ConstraintLayout datepickLayout = view.findViewById(R.id.date_filter_booked);
        switchState = fSwitch.isChecked();

        sectionModelArrayList = new ArrayList<>();
        recyclerView =  view.findViewById(R.id.booked_rview);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        sadapter = new DateSectionUserRecyclerViewAdapter("booked",this.getContext(), sectionModelArrayList);
        Log.d("DATETEST9",""+sadapter);
        recyclerView.setAdapter(sadapter);

        if (mAuth.getCurrentUser() != null) {

            futsal_id = mAuth.getCurrentUser().getUid();
            loadUsercalss();
            fSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked){
                       datepickLayout.setVisibility(View.INVISIBLE);
                       loadToRecyclerViewWhenOn(sadapter);
                   }else{
                       datepickLayout.setVisibility(View.VISIBLE);
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
//                        loadDataToRecyclerView(sadapter);
                        loadToRecyclerViewWhenOff(sadapter);
                        //adapter.notifyDataSetChanged();
                        //fDatePicker.setText(MONTHS[mMonth]+" "+ mDayOfMonth +", "+mYear);
                    }


                },day,month,year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();

            }


        });


        return view;
    }

    private void loadToRecyclerViewWhenOff(DateSectionUserRecyclerViewAdapter sadapter){
        mDatabase.collection("futsal_list").document(futsal_id).collection("booked")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (snapshot != null) {
                            sectionModelArrayList.clear();
                            for (QueryDocumentSnapshot document : snapshot) {
                                String pdate = document.getId();
                                Log.d("NEWREQTEST2.0.0", "onComplete: " + pdate + "-" + document.getData().get(pdate)+"_"+compareDate(pdate,date));
                                if (pdate.equals(date)) {
                                    Map<String, Object> dd1 = (Map<String, Object>) document.getData();
                                    r_list = new ArrayList<>();
                                    r_list.clear();
                                    for(String uid: dd1.keySet()){
                                        for (int i = 0; i < users_list.size(); i++) {
                                            Log.d("NEWREQTEST2.2", "onComplete: " + users_list.size() + " -- " + users_list.get(i).user_id + " -- " + uid);
                                            if (users_list.get(i).user_id.equals(uid)) {
                                                Log.d("NEWREQTEST2.3", "onComplete: " + dd1.get(uid));
                                                Map<String, Object> dd2 = (Map<String, Object>) dd1.get(uid);
                                                for (String time : dd2.keySet()) {
                                                    if (compareTimee(pdate, time)) {
                                                        BookingUser user = new BookingUser();
                                                        Log.d("NEWREQTEST2.4", "onComplete: " + time);
                                                        user.setTime(time);
                                                        user.setUser_full_name(users_list.get(i).getUser_full_name());
                                                        user.setUser_id(users_list.get(i).getUser_id());
                                                        user.setUser_address(users_list.get(i).getUser_address());
                                                        user.setUser_phone_number(users_list.get(i).getUser_phone_number());
                                                        user.setUser_profile_image(users_list.get(i).getUser_profile_image());
                                                        Log.d("NEWREQTEST2.5", "onComplete1: " + user.getTime());
                                                        r_list.add(user);
                                                    }
                                                }

                                                Log.d("NEWTEST2.1", "onComplete: " + r_list);
                                            }
                                        }
                                    }
                                    if(r_list.size() != 0) {
                                        sectionModelArrayList.add(new SectionModel(pdate,null, r_list));
                                        sadapter.notifyDataSetChanged();
                                        //firstload = false;
//
                                    }

                                }
                            }
                            sadapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void loadToRecyclerViewWhenOn(DateSectionUserRecyclerViewAdapter sadapter){
        mDatabase.collection("futsal_list").document(futsal_id).collection("booked")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (snapshot != null) {
                            sectionModelArrayList.clear();
                            for (QueryDocumentSnapshot document : snapshot) {
                                String pdate = document.getId();
                                Log.d("NEWREQTEST2.0.0", "onComplete: " + pdate + "-" + document.getData().get(pdate)+"_"+compareDate(pdate,date));
                                if (compareDate(pdate, currentDate)) {
                                    Map<String, Object> dd1 = (Map<String, Object>) document.getData();
                                    r_list = new ArrayList<>();
                                    r_list.clear();
                                    for(String uid: dd1.keySet()){
                                        for (int i = 0; i < users_list.size(); i++) {
                                            Log.d("NEWREQTEST2.2", "onComplete: " + users_list.size() + " -- " + users_list.get(i).user_id + " -- " + uid);
                                            if (users_list.get(i).user_id.equals(uid)) {
                                                Log.d("NEWREQTEST2.3", "onComplete: " + dd1.get(uid));
                                                Map<String, Object> dd2 = (Map<String, Object>) dd1.get(uid);
                                                for (String time : dd2.keySet()) {
                                                    if (compareTimee(pdate, time)) {
                                                        BookingUser user = new BookingUser();
                                                        Log.d("NEWREQTEST2.4", "onComplete: " + time);
                                                        user.setTime(time);
                                                        user.setUser_full_name(users_list.get(i).getUser_full_name());
                                                        user.setUser_id(users_list.get(i).getUser_id());
                                                        user.setUser_address(users_list.get(i).getUser_address());
                                                        user.setUser_phone_number(users_list.get(i).getUser_phone_number());
                                                        user.setUser_profile_image(users_list.get(i).getUser_profile_image());
                                                        Log.d("NEWREQTEST2.5", "onComplete1: " + user.getTime());
                                                        r_list.add(user);
                                                    }
                                                }

                                                Log.d("NEWTEST2.1", "onComplete: " + r_list);
                                            }
                                        }
                                    }
                                    if(r_list.size() != 0) {
                                        sectionModelArrayList.add(new SectionModel(pdate,null, r_list));
                                        sadapter.notifyDataSetChanged();
                                        //firstload = false;
//
                                    }

                                }
                            }
                            sadapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void loadUsercalss() {
        users_list = new ArrayList<>();
        mDatabase.collection("users_list").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String userid = documentSnapshot.getId();
                    BookingUser user = documentSnapshot.toObject(BookingUser.class);
                    Log.d("DATETEST4", "" + user);
                    user.setUser_id(userid);

                    users_list.add(user);
                    Log.d("NEWTEST2", "onComplete: " + users_list.size());

                }
                //loadDataToRecyclerView(sadapter);
                loadToRecyclerViewWhenOff(sadapter);
            }


        });
    }


    public Boolean compareDate(String pdate,String date) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        Date date1 = sdf.parse(pdate);
        Date date2 = sdf.parse(date);
//        if(date1.before(date2)){
//            Log.e("app", "Date1 is before Date2");
//            return true ;
//        }
        if (date1.equals(date2) || date1.after(date2)) {
            Log.e("APPTEST", "Date1 is after Date2");
            return true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;

}
    public Boolean compareTimee(String date ,String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hha", Locale.US);
            Date current = new Date();
            Date given = sdf.parse(date+" "+time);
//        if(date1.before(date2)){
//            Log.e("app", "Date1 is before Date2");
//            return true ;
//        }
            Log.d("BOOKEDTEST3.0", "compareTimee: "+current+"__"+given+"__"+given.after(current));
            if (given.after(current)) {
                Log.e("APPTEST", "given is after current");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}
