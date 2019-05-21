package com.example.futsalnepal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.example.futsalnepal.Model.Futsal;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class SearchLayout extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    DatePickerDialog dpd;
    private List<Futsal> futsalList, newList;
    private TextView dateSearch,timeSearch;
    private EditText searchByName,searchByLocation;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FutsalRecycleView adapter;
    private String date;

    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        //for toolbar
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseFirestore.getInstance();

        dateSearch = findViewById(R.id.date_search);
        timeSearch = findViewById(R.id.time_search);
        searchByName =findViewById(R.id.futsal_name_search);
        searchByLocation = findViewById(R.id.location_search);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        date = sdf.format(new Date());
        dateSearch.setText(date);

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
                        //dateSearch.setText(MONTHS[mMonth]+" "+ mDayOfMonth +","+mYear);
                        now.set(Calendar.YEAR, mYear);
                        now.set(Calendar.MONTH, mMonth);
                        now.set(Calendar.DAY_OF_MONTH, mDayOfMonth);

//                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
                        date = sdf.format(now.getTime());
                        dateSearch.setText(date);
                    }
                },day,month,year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });

        mDatabase.collection("futsal_list").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String futsalId = doc.getId();
                        Log.d("TESTING", "onEvent: " + doc);
                        Futsal futsals = doc.toObject(Futsal.class).withId(futsalId);
                        futsalList.add(futsals);
                    }
                }
            }
        });



        newList = new ArrayList<>();
        futsalList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.search_item_rview);
        adapter = new FutsalRecycleView(newList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        searchByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchItem();
            }

            @Override
            public void afterTextChanged(Editable s){
                startActivity(new Intent(SearchLayout.this,MapsActivity.class));
            }
        });


//        Places.initialize(getApplicationContext(), "AIzaSyAoCtPfy1MC-V_08UJSFKYNikt_hMAUWuQ");
//
//        // Create a new Places client instance.
//        PlacesClient placesClient = Places.createClient(this);
//
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i("PLACE", "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i("Error", "An error occurred: " + status);
//            }
//        });
    }

    public void SearchItem(){
        newList.clear();
        String searchTxt = searchByName.getText().toString().toLowerCase();
        if(!searchTxt.isEmpty()) {
            for (Futsal f : futsalList) {
                if (f.getFutsal_name().toLowerCase().contains(searchTxt)) {
                    newList.add(f);
                    adapter.notifyDataSetChanged();

                }
            }
        }
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
        SimpleDateFormat outputFormat = new SimpleDateFormat("hha",Locale.US);

        Date date1 ;
        String str1 = null;
        Date date2 ;
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
