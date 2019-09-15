package com.example.futsalnepal.futsal;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookTime;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalBookNowFragment extends Fragment {

    DatePickerDialog dpd;
    private RecyclerView bookTime;
    private TextView fDatePicker;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    String date;
    String futsal_id;
    List<BookTime> timeInHr;

    public FutsalBookNowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_book_now, container, false);

        ConstraintLayout placeHolder =  view.findViewById(R.id.include_book_now);
        getLayoutInflater().inflate(R.layout.fragment_book_time, placeHolder);

        Toolbar toolbar= view.findViewById(R.id.book_now_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //date = DateFormat.getDateInstance().format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        date = sdf.format(new Date());
        Activity a= this.getActivity();

        futsal_id = mAuth.getCurrentUser().getUid();
        fDatePicker = view.findViewById(R.id.date_picker_futsal);
        fDatePicker.setText(date);

        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        String openH = task.getResult().getString("opening_hour");
                        String closeH = task.getResult().getString("closing_hour");
                        Log.d("ARRAY13-", "onComplete: "+openH+"---"+closeH);
                        timeInHr =makeTimeArray(openH,closeH);
                        Log.d("ARRAY13-", "onComplete: "+timeInHr);
                        loadRecycler(view,date);
//                       -
                    }
                }

            }
        });


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
                        loadRecycler(view,date);

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

    private void loadRecycler(View view, String date1) {
        RecyclerView recyclerView =  view.findViewById(R.id.book_time_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        BookNowViewAdapter adapter = new BookNowViewAdapter(timeInHr,date,getContext(),getActivity());
        recyclerView.setAdapter(adapter);
    }

    public List<BookTime> makeTimeArray(String open, String close) {

        List<BookTime> timeArray = new ArrayList<>();
        timeArray.add(new BookTime("12AM"));
        timeArray.add(new BookTime("1AM"));
        timeArray.add(new BookTime("2AM"));
        timeArray.add(new BookTime("3AM"));
        timeArray.add(new BookTime("4AM"));
        timeArray.add(new BookTime("5AM"));
        timeArray.add(new BookTime("6AM"));
        timeArray.add(new BookTime("7AM"));
        timeArray.add(new BookTime("8AM"));
        timeArray.add(new BookTime("9AM"));
        timeArray.add(new BookTime("10AM"));
        timeArray.add(new BookTime("11AM"));
        timeArray.add(new BookTime("12PM"));
        timeArray.add(new BookTime("1PM"));
        timeArray.add(new BookTime("2PM"));
        timeArray.add(new BookTime("3PM"));
        timeArray.add(new BookTime("4PM"));
        timeArray.add(new BookTime("5PM"));
        timeArray.add(new BookTime("6PM"));
        timeArray.add(new BookTime("7PM"));
        timeArray.add(new BookTime("8PM"));
        timeArray.add(new BookTime("9PM"));
        timeArray.add(new BookTime("10PM"));
        timeArray.add(new BookTime("11PM"));

        List<BookTime> timeArray1;
        int openIdx=-1;
        int closeIdx=-1;
        for(int i = 0;i < timeArray.size();i++){
            if(timeArray.get(i).book_time.equals(open)){
                openIdx = i;
            }else if(timeArray.get(i).book_time.equals(close)){
                closeIdx =i;
            }

        }
        Log.d("ARRAY2",""+open+"  "+close );

        Log.d("ARRAY3", "makeTimeArray: "+openIdx+" "+closeIdx);
        if (openIdx <= closeIdx) {
            // straightforward case
            timeArray1 = timeArray.subList(openIdx, closeIdx);
            Log.d("ARRAY4"," "+timeArray1);
            return timeArray1;
        }
        return timeArray;

    }


}
