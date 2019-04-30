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

import com.example.futsalnepal.Model.BookTime;
import com.google.common.collect.Lists;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTimeFragment extends Fragment {
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatePickerDialog dpd;
    private RecyclerView bookTime;
    private TextView fDatePicker;
    String date;
    public BookTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_time, container, false);

        FutsalIndivisualDetails activity = (FutsalIndivisualDetails) getActivity();
        String futsal_id = activity.getMyData();

        date = DateFormat.getDateInstance().format(new Date());
        fDatePicker = view.findViewById(R.id.date_picker_futsal);
        fDatePicker.setText(date);
        List<BookTime> timeArray =makeTimeArray("3AM","5PM");
        Log.d("ARRAY",""+futsal_id +" - "+date);
        RecyclerView recyclerView =  view.findViewById(R.id.book_time_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        BookTimeViewAdapaer adapter = new BookTimeViewAdapaer(timeArray,date,futsal_id,getContext(),getActivity());
        recyclerView.setAdapter(adapter);

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
                        date = DateFormat.getDateInstance().format(now.getTime());
                        fDatePicker.setText(date);
                        BookTimeViewAdapaer adapter = new BookTimeViewAdapaer(timeArray,date,futsal_id,getContext(),getActivity());
                        recyclerView.setAdapter(adapter);
                        //adapter.notifyDataSetChanged();
                        //fDatePicker.setText(MONTHS[mMonth]+" "+ mDayOfMonth +","+mYear);
                    }
                },day,month,year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();

            }
        });



        return view;
    }

    public  List<BookTime> makeTimeArray(String open,String close) {

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


//        timeArray.add("12AM");
//        timeArray.add("1AM");
//        timeArray.add("2AM");
//        timeArray.add("3AM");
//        timeArray.add("4AM");
//        timeArray.add("5AM");
//        timeArray.add("6AM");
//        timeArray.add("7AM");
//        timeArray.add("8AM");
//        timeArray.add("9AM");
//        timeArray.add("10AM");
//        timeArray.add("11AM");
//        timeArray.add("12PM");
//        timeArray.add("1PM");
//        timeArray.add("2PM");
//        timeArray.add("3PM");
//        timeArray.add("4PM");
//        timeArray.add("5PM");
//        timeArray.add("6PM");
//        timeArray.add("7PM");
//        timeArray.add("8PM");
//        timeArray.add("9PM");
//        timeArray.add("10PM");
//        timeArray.add("11PM");
        List<BookTime> timeArray1;
        int openIdx=-1;
        int closeIdx=-1;
        for(int i = 0;i < timeArray.size();i++){
            if(timeArray.get(i).book_time == open){
                openIdx = i;
            }else if(timeArray.get(i).book_time == close){
                closeIdx =i;
            }

        }
        Log.d("ARRAY2",""+timeArray );

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
