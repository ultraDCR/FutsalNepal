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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    List<BookTime> timeArray;

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



        fDatePicker = view.findViewById(R.id.date_picker_futsal);

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
                        String myFormat = DateFormat.getDateInstance().format(now.getTime());

                        fDatePicker.setText(myFormat);
                        //fDatePicker.setText(MONTHS[mMonth]+" "+ mDayOfMonth +","+mYear);
                    }
                },day,month,year);
                dpd.show();

            }
        });

        makeTimeArray("6AM","5PM");
        Log.d("ARRAY",""+timeArray);
        RecyclerView recyclerView =  view.findViewById(R.id.book_time_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        BookTimeViewAdapaer adapter = new BookTimeViewAdapaer(timeArray,getContext(),getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<BookTime> makeTimeArray(String open,String close) {

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
        int openIdx = timeArray.indexOf(new BookTime(open));
        int closeIdx = timeArray.indexOf(new BookTime(close));
        Log.d("ARRAY1", "makeTimeArray: "+openIdx+" "+ closeIdx);
        if (openIdx <= closeIdx) {
            // straightforward case
            timeArray1 = new ArrayList<>(timeArray.subList(openIdx, closeIdx));
            return timeArray1;
        }
        return timeArray;

    }


}
