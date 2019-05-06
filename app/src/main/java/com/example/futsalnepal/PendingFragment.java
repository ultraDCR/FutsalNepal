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
import com.example.futsalnepal.Model.SectionModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PendingFragment extends Fragment {

    List<SectionModel> sectionModelArrayList;
    List<BookingFutsal> futsal_list;
    List<BookingFutsal> p_list;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;
    private String date;
    DatePickerDialog dpd;
    private TextView fDatePicker;
    private RecyclerView recyclerView;
    private DateSectionUserRecyclerViewAdapter sadapter;
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

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        date = sdf.format(new Date());

        fDatePicker = view.findViewById(R.id.date_picker_pending);
        fDatePicker.setText(date);

        sectionModelArrayList = new ArrayList<>();
        recyclerView =  view.findViewById(R.id.pending_rview);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

         sadapter = new DateSectionUserRecyclerViewAdapter("pending",this.getContext(), sectionModelArrayList);
        Log.d("DATETEST9",""+sadapter);
        recyclerView.setAdapter(sadapter);
        //PendingRequestRecyclerView adapter = new PendingRequestRecyclerView(futsal_list, getContext());
        if(mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            loadFutsalcalss();
        }



        fDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                int day = now.get(Calendar.YEAR); // Initial year selection
                int month = now.get(Calendar.MONTH); // Initial month selection
                int year = now.get(Calendar.DAY_OF_MONTH);// Inital ---day selection


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
                        loadDataToRecyclerView(sadapter);


                        //adapter.notifyDataSetChanged();
                        //fDatePicker.setText(MONTHS[mMonth]+" "+ mDayOfMonth +", "+mYear);
                    }


                },day,month,year);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();

            }


        });


        return view;
    }

    private void loadDataToRecyclerView(DateSectionUserRecyclerViewAdapter sadapter) {
        mDatabase.collection("user_list").document(user_id).collection("book_info").document("pending")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        Map<String, Object> dd = task.getResult().getData();
                        for(String pdate:dd.keySet()){
                            Log.d("TESTING@", "" + pdate);
                            if (compareDate(pdate, date)) {
                                Log.d("DATETEST2", "" + futsal_list);
                                Map<String, Object> dd1 = (Map<String, Object>) task.getResult().get(pdate);

                                p_list = new ArrayList<>();
                                for (String futsalid : dd1.keySet()) {
                                    if (futsalid != null) {
                                        Log.d("NEWTEST1", "" + futsalid);
                                        for(int i = 0;i < futsal_list.size();i++) {
                                            Log.d("NEWTEST2.0", "onComplete: " +futsal_list.size()+" -- " + futsal_list.get(1).futsal_id + " -- " + futsalid);
                                            if(futsal_list.get(i).futsal_id.equals(futsalid)){
                                                Log.d("NEWTEST2.2", "onComplete: "  + dd1.get(futsalid));
                                                Map<String, String> dd2 = (Map<String, String>) dd1.get(futsalid);
                                                for(String time: dd2.keySet()){

                                                    BookingFutsal futsal1 = new BookingFutsal();
                                                    Log.d("NEWTEST2.3", "onComplete: "  + time);
                                                    futsal1.setTime(time);
                                                    futsal1.setFutsal_name(futsal_list.get(i).getFutsal_name());
                                                    futsal1.setFutsal_id(futsal_list.get(i).getFutsal_id());
                                                    futsal1.setFutsal_address(futsal_list.get(i).getFutsal_address());
                                                    futsal1.setFutsal_phone(futsal_list.get(i).getFutsal_phone());
                                                    futsal1.setFutsal_logo(futsal_list.get(i).getFutsal_logo());
                                                    futsal1.setOverall_rating(futsal_list.get(i).getOverall_rating());
                                                    Log.d("NEWTEST2.3", "onComplete1: "  + futsal1.getTime());
                                                    p_list.add(futsal1);

                                                }

                                                Log.d("NEWTEST2.1", "onComplete: "  + p_list);
                                            }

                                        }

                                    }

                                }
                                Log.d("NEWTEST3", "onComplete: " + pdate + "  " + p_list);
                                sectionModelArrayList.add(new SectionModel(pdate, p_list));
                                sadapter.notifyDataSetChanged();
                                for(BookingFutsal book:p_list){
                                    Log.d("NEWTEST4", "onComplete: " + book.getTime() + "  " + book.getFutsal_name());
                                }
                                //p_list.clear();
                                Log.d("NEWTEST4", "onComplete: " + pdate + "  " + p_list);
                                Log.d("DATETEST7", "onComplete: " + sectionModelArrayList);
                            }

                        }
                    }
                }
            }
        });


    }

    private void loadFutsalcalss() {
        futsal_list = new ArrayList<>();
        mDatabase.collection("futsal_list").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    String futsalid = documentSnapshot.getId();
                    BookingFutsal futsal = documentSnapshot.toObject(BookingFutsal.class);
                    Log.d("DATETEST4", "" + futsal);
                    futsal.setFutsal_id(futsalid);

                    futsal_list.add(futsal);
                    Log.d("NEWTEST2", "onComplete: "  + futsal_list.size());

                }
                loadDataToRecyclerView(sadapter);
            }


        });
    }


    public Boolean compareDate(String pdate,String date ){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            Date date1 = sdf.parse(pdate);
            Date date2 = sdf.parse(date);
//        if(date1.before(date2)){
//            Log.e("app", "Date1 is before Date2");
//            return true ;
//        }
            if(date1.equals(date2) || date1.after(date2)){
                Log.e("APPTEST", "Date1 is after Date2");
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }



//    public void futureEvents() {
//        ArrayList<Futsal> futureEvents = new ArrayList<>();
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
//        String currentDate = formatter.format(new Date());
//
//        for(Futsal events : futsal_list) {
//
//            Date date = null;
//            try {
//                date = formatter.parse(dateFutsal);
//            } catch (ParseException e) {
//
//            }
//
//            if(currentDate.before(date)) {
//                futureEvents.add(events);
//            }
//        }
//
//        adapter.filter(futureEvents);
//    }

}
