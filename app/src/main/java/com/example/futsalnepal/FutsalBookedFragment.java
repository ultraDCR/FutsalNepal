package com.example.futsalnepal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FutsalBookedFragment extends Fragment {
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id;
    private String date;
    DatePickerDialog dpd;
    private TextView fDatePicker;
    private RecyclerView recyclerView;
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
}
