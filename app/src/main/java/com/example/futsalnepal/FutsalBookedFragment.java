package com.example.futsalnepal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookingFutsal;
import com.example.futsalnepal.Model.BookingUser;
import com.example.futsalnepal.Model.SectionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FutsalBookedFragment extends Fragment {

    List<SectionModel> sectionModelArrayList;
    List<BookingUser> user_list;
    List<BookingUser> r_list;

    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String futsal_id;
    private String date;
    DatePickerDialog dpd;
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

        fDatePicker = view.findViewById(R.id.date_picker_booked);
        fDatePicker.setText(date);

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
                        loadToRecyclerView(sadapter);
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
//    private void loadDataToRecyclerView(DateSectionUserRecyclerViewAdapter sadapter) {
//        mDatabase.collection("futsal_list").document(futsal_id).collection("book_info").document("booked")
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().exists()) {
//                        Map<String, Object> dd = task.getResult().getData();
//                        for (String pdate : dd.keySet()) {
//                            Log.d("TESTING@", "" + pdate);
//                            if (compareDate(pdate, date)) {
//                                Log.d("DATETEST2", "" + user_list);
//                                Map<String, Object> dd1 = (Map<String, Object>) task.getResult().get(pdate);
//
//                                r_list = new ArrayList<>();
//                                for (String userid : dd1.keySet()) {
//                                    if (userid != null) {
//                                        Log.d("NEWTEST1", "" + userid);
//                                        for (int i = 0; i < user_list.size(); i++) {
//                                            Log.d("NEWTEST2.0", "onComplete: " + user_list.size() + " -- " + user_list.get(1).user_id + " -- " + userid);
//                                            if (user_list.get(i).user_id.equals(userid)) {
//                                                Log.d("NEWTEST2.2", "onComplete: " + dd1.get(userid));
//                                                Map<String, String> dd2 = (Map<String, String>) dd1.get(userid);
//                                                for (String time : dd2.keySet()) {
//                                                    if(compareTimee(time)) {
//                                                        BookingUser user = new BookingUser();
//                                                        Log.d("NEWTEST2.3", "onComplete: " + time);
//                                                        user.setTime(time);
//                                                        user.setUser_full_name(user_list.get(i).getUser_full_name());
//                                                        user.setUser_id(user_list.get(i).getUser_id());
//                                                        user.setUser_address(user_list.get(i).getUser_address());
//                                                        user.setUser_phone_number(user_list.get(i).getUser_phone_number());
//                                                        user.setUser_profile_image(user_list.get(i).getUser_profile_image());
//                                                        Log.d("NEWTEST2.3", "onComplete1: " + user.getTime());
//                                                        r_list.add(user);
//                                                    }
//                                                }
//
//                                                Log.d("NEWTEST2.1", "onComplete: " + r_list);
//                                            }
//
//                                        }
//
//                                    }
//
//                                }
//                                Log.d("NEWTEST3", "onComplete: " + pdate + "  " + r_list);
//                                sectionModelArrayList.add(new SectionModel(pdate, null, r_list));
//                                sadapter.notifyDataSetChanged();
//                                //r_list.clear();
//                                Log.d("NEWTEST4", "onComplete: " + pdate + "  " + r_list);
//                                Log.d("DATETEST7", "onComplete: " + sectionModelArrayList);
//                            }
//
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }


    private void loadToRecyclerView(DateSectionUserRecyclerViewAdapter sadapter){
        mDatabase.collection("futsal_list").document(futsal_id).collection("book_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("FIREBASETEST5", "onComplete: "+task.getResult().getDocuments());

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String pdate = document.getId();
                                Log.d("FIREBASETEST4", "onComplete: "+pdate+"-"+document.getDocumentReference(pdate)+"-"+document.getReference());
                                Log.d("BOOKEDTEST2.00", "onComplete: "+compareDate(pdate,date)+"__"+date+"___"+pdate);
                                if (compareDate(pdate, date)) {
                                    Log.d("BOOKEDTEST2.10", "onComplete: "+compareDate(pdate,date));
                                    document.getReference().collection("booked").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                r_list = new ArrayList<>();
                                                Log.d("BOOKEDTEST2.0", "onComplete: " + task.getResult().getDocuments());
                                                for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                    String userid = document1.getId();
                                                    for (int i = 0; i < user_list.size(); i++) {
                                                        Log.d("BOOKEDTEST2.0", "onComplete: " + user_list.size() + " -- " + user_list.get(i).user_id + " -- " + userid);
                                                        if (user_list.get(i).user_id.equals(userid)) {
                                                            //Log.d("NEWTEST2.2", "onComplete: " + dd1.get(userid));
                                                            Map<String, Object> dd2 = (Map<String, Object>) document1.get("time");
                                                            for (String time : dd2.keySet()) {
                                                                Log.d("BOOKEDTEST2.2", "onComplete: " + time+"__"+compareTimee(pdate,time));
                                                                if (compareTimee(pdate, time)) {
                                                                    BookingUser user = new BookingUser();
                                                                    Log.d("BOOKEDTEST2.3", "onComplete: " + time);
                                                                    user.setTime(time);
                                                                    user.setUser_full_name(user_list.get(i).getUser_full_name());
                                                                    user.setUser_id(user_list.get(i).getUser_id());
                                                                    user.setUser_address(user_list.get(i).getUser_address());
                                                                    user.setUser_phone_number(user_list.get(i).getUser_phone_number());
                                                                    user.setUser_profile_image(user_list.get(i).getUser_profile_image());
                                                                    Log.d("BOOKEDTEST2.4", "onComplete1: " + user.getTime());
                                                                    r_list.add(user);
                                                                }
                                                            }

                                                            Log.d("BOOKEDTEST2.1", "onComplete: " + r_list);
                                                        }
                                                    }
                                                }
                                                if(r_list.size() != 0) {
                                                    sectionModelArrayList.add(new SectionModel(pdate,null, r_list));
                                                    sadapter.notifyDataSetChanged();
                                                }
                                            }else{
                                                Log.d("FIREBASEERROR", "ERROR ON RETERIVAL: ");
                                            }
                                        }
                                    });

                                }
                            }
                        } else {
                            Log.d("ERROR IN RETRIVAL", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void loadUsercalss() {
        user_list = new ArrayList<>();
        mDatabase.collection("user_list").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String userid = documentSnapshot.getId();
                    BookingUser user = documentSnapshot.toObject(BookingUser.class);
                    Log.d("DATETEST4", "" + user);
                    user.setUser_id(userid);

                    user_list.add(user);
                    Log.d("NEWTEST2", "onComplete: " + user_list.size());

                }
                //loadDataToRecyclerView(sadapter);
                loadToRecyclerView(sadapter);
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
