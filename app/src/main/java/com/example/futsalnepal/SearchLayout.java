package com.example.futsalnepal;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.futsal.FutsalInfoEdit;
import com.example.futsalnepal.futsal.SpinnerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class SearchLayout extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    DatePickerDialog dpd;
    private Spinner pSpinner, dSpinner, vSpinner;
    private JSONObject n = null;
    private JSONObject d = null;
    private ArrayList<String> clist, dlist, vlist;
    private SpinnerAdapter dadapter, vadapter;
    String distric, vdc, provienc;
    private List<Futsal> futsalList, newList;
    private TextView dateSearch,timeSearch,searchByLocation;
    private EditText searchByName;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FutsalRecycleView adapter;
    private String date;

    //public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        //for toolbar
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        pSpinner = findViewById(R.id.provienc_search);
        dSpinner = findViewById(R.id.district_search);
        vSpinner = findViewById(R.id.vdc_search);

        clist = new ArrayList<>();
        loadLocatonSpinner();

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

            }
        });

        searchByLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchItem();
            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    protected void onStart() {
        super.onStart();
        findViewById( R.id.search_views ).requestFocus();
    }

    public void SearchItem(){

        Log.d("SEARCH", "SearchItem: "+vdc+"   "+distric+"   "+provienc);
        String searchTxt = searchByName.getText().toString().toLowerCase();
        String location = searchByLocation.getText().toString();
        if(!searchTxt.equals("")) {
            newList.clear();
            for (Futsal f : futsalList) {
                if (f.getFutsal_name().toLowerCase().contains(searchTxt)) {
                    Log.d("SEARCH", "SearchItem: "+f.getLocation()+"   "+provienc);
                    searchByLocation(f,1);
                }
            }
        }else {
            newList.clear();
            for (Futsal f : futsalList) {
                Log.d("SEARCH", "SearchItem: " + f.getLocation() + "   " + provienc);
                searchByLocation(f,2);
            }
        }
    }

    private void searchByLocation(Futsal f,int i){
        if(f.getLocation().get("province").equals(provienc)){
            String dis = f.getLocation().get("district").toString();
            String v =  f.getLocation().get("vdc").toString();

            if(dis.equals(distric)){
                if(v.equals(vdc)){
                    newList.add(f);
                    adapter.notifyDataSetChanged();
                }else if(vdc.equals("-- select the VDC --")){
                    newList.add(f);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter.notifyDataSetChanged();
                }
            }else if(distric.equals("-- select the district --")){
                newList.add(f);
                adapter.notifyDataSetChanged();
            }else{
                adapter.notifyDataSetChanged();
            }
        }else if (provienc.equals("-- select the province --" ) && i==1){
            newList.add(f);
            adapter.notifyDataSetChanged();
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

    private void loadLocatonSpinner() {

        String hello = loadJSONFromAsset(SearchLayout.this);

        try {
            n = new JSONObject(hello);
            Log.d("JSONFILE", "onCreate: " + n);
            clist.clear();
            clist.add(0, "-- select the province --");
            clist = findKeysOfJsonObject(n, clist);
            SpinnerAdapter adapter = new SpinnerAdapter(clist, SearchLayout.this);
            pSpinner.setAdapter(adapter);
            pSpinner.setDropDownVerticalOffset(100);

            pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //((TextView)parent.getChildAt(position)).setTextColor(Color.RED);

                    provienc = clist.get(position);

                    try {
                        dlist = new ArrayList<>();
                        dadapter = new SpinnerAdapter(dlist, SearchLayout.this);
                        dSpinner.setAdapter(dadapter);
                        dSpinner.setDropDownVerticalOffset(100);
                        dlist.clear();
                        dlist.add(0, "-- select the district --");
                        if (provienc.equals("-- select the province --")) {
                            searchByLocation.setText("");
                            dadapter.notifyDataSetChanged();
                        } else {
                            searchByLocation.setText(provienc);
                            d = n.getJSONObject(provienc);
                            dlist = findKeysOfJsonObject(d, dlist);
                            dadapter.notifyDataSetChanged();
                        }


                        Log.d("LISTCHECK", "onItemSelected: " + clist);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    dlist.clear();
                    dlist.add(0, "-- select the district --");
                    dadapter.notifyDataSetChanged();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


        dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distric = dlist.get(position);
                String p1 =distric+ ", " + provienc;


                try {
                    vlist = new ArrayList<>();
                    vadapter = new SpinnerAdapter(vlist, SearchLayout.this);
                    vSpinner.setAdapter(vadapter);
                    vSpinner.setDropDownVerticalOffset(100);
                    vlist.clear();
                    vlist.add(0, "-- select the VDC --");
                    if (distric.equals("-- select the district --")) {
                        searchByLocation.setText(provienc);
                        if(provienc.equals("-- select the province --")) {
                            searchByLocation.setText("");
                        }
                        vadapter.notifyDataSetChanged();
                    } else {
                        searchByLocation.setText(p1);
                        JSONArray v = d.getJSONArray(distric);
                        Log.d("LISTCHECK1", "onItemSelected: " + v);
                        for (int i = 0; i < v.length(); i++) {
                            vlist.add(v.get(i).toString());
                        }
                        vadapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vlist.clear();
                vlist.add(0, "-- select the VDC --");
                vadapter.notifyDataSetChanged();
            }
        });

        vSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vdc = vlist.get(position);
                String p = distric + ", " + provienc;
                String p1 = vdc + ", " + p;
                if (vdc.equals("-- select the VDC --")) {
                    searchByLocation.setText(p);
                    if(distric.equals("-- select the district --")){
                        searchByLocation.setText(provienc);
                        if(provienc.equals("-- select the province --")){
                            searchByLocation.setText("");
                        }
                    }

                } else {
                    searchByLocation.setText(p1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Provience.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private static ArrayList<String> findKeysOfJsonObject(JSONObject jsonIn, ArrayList<String> keys) {

        Iterator<String> itr = jsonIn.keys();
        ArrayList<String> keysFromObj = makeList(itr);
        keys.addAll(keysFromObj);
        return keys;
    }

    public static ArrayList<String> makeList(Iterator<String> iter) {
        ArrayList<String> list = new ArrayList<String>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }


}
