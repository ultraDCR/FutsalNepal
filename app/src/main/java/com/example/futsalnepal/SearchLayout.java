package com.example.futsalnepal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchLayout extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    DatePickerDialog dpd;
    private TextView dateSearch,timeSearch;
    private EditText searchByName,searchByLocation;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        //for toolbar
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dateSearch = findViewById(R.id.date_search);
        timeSearch = findViewById(R.id.time_search);
        searchByName =findViewById(R.id.futsal_name_search);
        searchByLocation = findViewById(R.id.location_search);

        //time picker
        timeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        SearchLayout.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        dateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                 int day = now.get(Calendar.YEAR); // Initial year selection
                 int month = now.get(Calendar.MONTH); // Initial month selection
                 int year = now.get(Calendar.DAY_OF_MONTH);// Inital day selection

                dpd = new DatePickerDialog(SearchLayout.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDayOfMonth) {
                        dateSearch.setText(MONTHS[mDayOfMonth]+" "+ mMonth +","+mYear);
                    }
                },day,month,year);
                dpd.show();
            }
        });

    }
    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay,int minute, int hourOfDayEnd, int minuteEnd) {


        SimpleDateFormat inputFormat = new SimpleDateFormat("HH");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh a");

        Date date1 = null;
        String str1 = null;
        Date date2 = null;
        String str2 = null;

        try {
            date1 = inputFormat.parse(""+hourOfDay);
            str1 = outputFormat.format(date1);
            date2 = inputFormat.parse(""+hourOfDayEnd);
            str2 = outputFormat.format(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String time = str1+" - "+str2;
        timeSearch.setText(time);

    }



}
