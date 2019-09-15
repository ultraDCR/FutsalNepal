package com.example.futsalnepal;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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


import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.futsal.SpinnerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import static com.example.futsalnepal.AppConstants.LOCATION_REQUEST;

public class SearchLayout extends AppCompatActivity implements com.example.futsalnepal.TimePickerDialog.EditDialogListener {
    DatePickerDialog dpd;
    private Location currentLocation;
    private boolean isGPS =false;
    double lang ,latu ;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private Spinner pSpinner, dSpinner, vSpinner;
    private JSONObject n = null;
    private JSONObject d = null;
    private ArrayList<String> clist, dlist, vlist;
    private List<String> timeList;
    private SpinnerAdapter dadapter, vadapter;
    String distric, vdc, provienc;
    private List<Futsal> futsalList, newList;
    private TextView dateSearch,timeSearch,searchByLocation;
    private EditText searchByName;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private FutsalRecycleView adapter;
    private String date;
    private String fromTime = null;
    private String toTime = null;


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
        timeList = new ArrayList<>();
        new GpsUtils(SearchLayout.this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            isGPS = isGPSEnable;
        });

        loadLocatonSpinner();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        date = sdf.format(new Date());
        dateSearch.setText(date);


        //time picker
        timeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                TimePickerDialog tpd = TimePickerDialog.newInstance(
//                        SearchLayout.this,
//                        now.get(Calendar.HOUR_OF_DAY),
//                        now.get(Calendar.MINUTE),
//                        false
//                );
//                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        Log.d("TimePicker", "Dialog was cancelled");
//                    }
//                });
//
                DialogFragment newFragment = com.example.futsalnepal.TimePickerDialog.newInstance();
                newFragment.show(getSupportFragmentManager(),"timepicker");
            }
        });
        Calendar now = Calendar.getInstance();
        dateSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        loadPendingAndBooked();
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
                    futsalList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String futsalId = doc.getId();
                        Log.d("TESTING", "onEvent: " + doc);
                        Futsal futsals = doc.toObject(Futsal.class).withId(futsalId);
                        futsalList.add(futsals);

                    }
                    distanceCalculationAndSort();
                    loadPendingAndBooked();

                }
            }
        });



        newList = new ArrayList<>();
        futsalList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.search_item_rview);
        adapter = new FutsalRecycleView(newList,this,this);
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

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    distanceCalculationAndSort();
                } else {
                    Toast.makeText(SearchLayout.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Dstance calculation and sort start
    private void distanceCalculationAndSort() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SearchLayout.this);
            if (ActivityCompat.checkSelfPermission(SearchLayout.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchLayout.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SearchLayout.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                return;
            }
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        currentLocation = location;
                        lang= currentLocation.getLongitude();
                        latu = currentLocation.getLatitude();
                        if(isGPS){
                            storeAndSort();
                        }else{
                            Toast.makeText(SearchLayout.this, "Turn on the GPS to find the distance between you and futsal and sort by nearest.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        //Toast.makeText(SearchLayout.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                    }
                }


            });


    }

    private void storeAndSort() {
        for(Futsal f : futsalList) {
            double distance = (distance((double) f.getLocation().get("latitude"), (double) f.getLocation().get("longitude"), latu, lang))*1000;
            f.setDistance(distance);
        }
        Collections.sort(futsalList,
                (o1, o2) -> Double.valueOf(o1.getDistance()).compareTo(Double.valueOf(o2.getDistance()))
        );
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Log.d("DISTANCE", "distance: "+dist);
        return (dist);

    }

    private double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

//    End of distance calculaion and sort

    @Override
    protected void onStart() {
        super.onStart();
        findViewById( R.id.search_views ).requestFocus();
        if (ActivityCompat.checkSelfPermission(SearchLayout.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchLayout.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchLayout.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
    }

    public void SearchItem(){
        distanceCalculationAndSort();
        Log.d("SEARCH", "SearchItem: "+vdc+"   "+distric+"   "+provienc);
        String searchTxt = searchByName.getText().toString().toLowerCase();
        String location = searchByLocation.getText().toString();
        String dateSearch = date;
        Futsal f1;
        newList.clear();
        for (Futsal f : futsalList) {
            f1= textSearch(searchTxt,f);
            if(f1!=null)
                f1 = searchByLocation(f1, 2);
            if(f1 != null)
                dateFilter(f1);
        }

//        if(!searchTxt.equals("")) {
//            newList.clear();
//            for (Futsal f : futsalList) {
//                if (f.getFutsal_name().toLowerCase().contains(searchTxt)) {
//                    Log.d("SEARCH", "SearchItem: "+f.getLocation()+"   "+provienc);
//                    Futsal fut = searchByLocation(f,2);
//                    if(fut != null) {
//                        dateFilter(fut);
//                    }else{
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//            }
//        }else if(provienc.equals("-- select the province --" ) && searchTxt.equals("") ) {
//            newList.clear();
//            for (Futsal f : futsalList) {
//                Log.d("SEARCH", "SearchItem: " + f.getLocation() + "   " + provienc);
//                    dateFilter(f);
//
////                    adapter.notifyDataSetChanged();
//
//            }
//        }
//        else{
//            newList.clear();
//            for (Futsal f : futsalList) {
//                Log.d("SEARCH", "SearchItem: " + f.getLocation() + "   " + provienc);
//                Futsal fut = searchByLocation(f,2);
//                if(fut != null) {
//                    dateFilter(fut);
//                }else{
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
    }

    private Futsal textSearch(String searchTxt, Futsal f) {
        if (f.getFutsal_name().toLowerCase().contains(searchTxt)) {
            Log.d("SEARCH", "SearchItem: "+f.getLocation()+"   "+provienc);
            return f;
        }
        return null;
    }


    private void dateFilter(Futsal fut){
        timeList = makeTimeArray(fut.getOpening_hour(),fut.getClosing_hour(),"time");
        Log.d("HELLO", "SearchItem: "+timeList+" 0 "+fut.getPendingtime()+"  0  "+fut.getBookedtime());
        if(fut.getPendingtime() != null){
            for(String time :fut.getPendingtime()){
                timeList.remove(time);
            }
        }
        if(fut.getBookedtime() != null) {
            for (String time : fut.getBookedtime()) {
                timeList.remove(time);
            }
        }

        int number = 0;
        List<String> inputTime = new ArrayList<>();
        if (fromTime != null && toTime != null) {
            inputTime = makeTimeArray(fromTime, toTime,"input");
            Log.d("HELLO1", "dateFilter: " + inputTime);
            for (String t : inputTime) {
                if (timeList.contains(t)) {
                    number ++;
                }
            }
            if(timeList.size()>1 && number != 0 ){
                newList.add(fut);
                adapter.notifyDataSetChanged();
            }
            else{
                Log.d("HELLO3", "dateFilter: " + number);
                adapter.notifyDataSetChanged();
            }
        }else{
            if(timeList.size()>1){
                newList.add(fut);
                adapter.notifyDataSetChanged();
            }
            else{
                adapter.notifyDataSetChanged();
            }
        }

        Log.d("HELLO2", "dateFilter: "+timeList);

    }

    private void loadPendingAndBooked(){
        timeList.clear();
        loadPending();
        loadBooked();
    }

    private void loadPending() {

        for(Futsal f: futsalList) {
            f.setPendingtime(null);
            mDatabase.collection("futsal_list").document(f.FutsalId).
                    collection("newrequest").document(date).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        List<String> btime;
                        btime = new ArrayList<>();

                        Map<String, Object> userIdMap = documentSnapshot.getData();
                        if (userIdMap != null) {
                            for (String user_id : userIdMap.keySet()) {
                                Map<String, Object> timeMap = (Map<String, Object>) userIdMap.get(user_id);
                                for (String time : timeMap.keySet()) {
                                    btime.add(time);
                                }
                            }
                            f.setPendingtime(btime);
                            SearchItem();
                            Log.d("HELLO!!", "onEvent: InSIDE"+date);
                        }
                    }
                }


            });
            SearchItem();
        }

    }

    private void loadBooked(){
        for(Futsal f: futsalList) {
            f.setBookedtime(null);
            mDatabase.collection("futsal_list").document(f.FutsalId).
                    collection("booked").document(date).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {
                        List<String> btime;
                        btime = new ArrayList<>();
                        Map<String, Object> userIdMap = documentSnapshot.getData();
                        if (userIdMap != null) {
                            for (String user_id : userIdMap.keySet()) {
                                Map<String, Object> timeMap = (Map<String, Object>) userIdMap.get(user_id);
                                for (String time : timeMap.keySet()) {
                                    btime.add(time);
                                }
                            }
                            f.setBookedtime(btime);
                            SearchItem();
                        }
                    }
                }
            });
            SearchItem();
        }

    }

    private Futsal searchByLocation(Futsal f,int i){
//        Log.d("HELLO#", "searchByLocation: "+provienc);
        if(f.getLocation().get("province").equals(provienc)){
            String dis = f.getLocation().get("district").toString();
            String v =  f.getLocation().get("vdc").toString();

            if(dis.equals(distric)){
                if(v.equals(vdc)){
                    return f;
                }else if(vdc.equals("-- select the VDC --")){
                    return f;
                }else{
                    return null;
                }
            }else if(distric.equals("-- select the district --")){
                return f;
            }else{
                return null;
            }
        }
        else if (provienc.equals("-- select the province --" )){
            return f;
        }
        return null;
    }

        // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


//    @Override
//    public void onTimeSet(RadialPickerLayout view, int hourOfDay,int minute, int hourOfDayEnd, int minuteEnd) {
//        SimpleDateFormat inputFormat = new SimpleDateFormat("HH");
//        SimpleDateFormat outputFormat = new SimpleDateFormat("ha",Locale.US);
//        Date date1 ,date2 ;
//        try {
//            date1 = inputFormat.parse(""+hourOfDay);
//            fromTime = outputFormat.format(date1);
//            date2 = inputFormat.parse(""+hourOfDayEnd);
//            toTime = outputFormat.format(date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String time = fromTime+" - "+toTime;
//        timeSearch.setText(time);
//        SearchItem();
//    }

    //Start of location spinner
    private void loadLocatonSpinner() {

        String hello = loadJSONFromAsset(SearchLayout.this);
        provienc ="";
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
//end of location spinner


    public static ArrayList<String> makeList(Iterator<String> iter) {
        ArrayList<String> list = new ArrayList<String>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    public  List<String> makeTimeArray(String open, String close, String type) {

        List<String> timeArray = new ArrayList<>();
        timeArray.add("12AM");
        timeArray.add("1AM");
        timeArray.add("2AM");
        timeArray.add("3AM");
        timeArray.add("4AM");
        timeArray.add("5AM");
        timeArray.add("6AM");
        timeArray.add("7AM");
        timeArray.add("8AM");
        timeArray.add("9AM");
        timeArray.add("10AM");
        timeArray.add("11AM");
        timeArray.add("12PM");
        timeArray.add("1PM");
        timeArray.add("2PM");
        timeArray.add("3PM");
        timeArray.add("4PM");
        timeArray.add("5PM");
        timeArray.add("6PM");
        timeArray.add("7PM");
        timeArray.add("8PM");
        timeArray.add("9PM");
        timeArray.add("10PM");
        timeArray.add("11PM");

        SimpleDateFormat sdf = new SimpleDateFormat("ha", Locale.US);
        String currentTime = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        String currentDate = sdf1.format(new Date());

        List<String> timeArray1;
        int openIdx=-1;
        int closeIdx=-1;

        Log.d("HELLO@32", "makeTimeArray: "+timeArray.indexOf(currentTime));
        if(type.equals("time")) {
            if(currentDate.equals(date)) {
                openIdx = timeArray.indexOf(currentTime);

            }else{
                openIdx = timeArray.indexOf(open);
            }
            closeIdx = timeArray.indexOf(close);
        }
        if(type.equals("input")){
            openIdx = timeArray.indexOf(open);
            closeIdx = timeArray.indexOf(close);
        }
        if (openIdx <= closeIdx) {
            // straightforward case
            timeArray1 = timeArray.subList(openIdx, closeIdx);
            Log.d("ARRAY4"," "+timeArray1);
            return timeArray1;
        }
        return timeArray;

    }


    @Override
    public void updateResult(String from, String to) {
        fromTime = from;
        toTime = to;
        String time;
        if(fromTime == null&& toTime==null){
           time = "Any Time";
        }else {
            time = fromTime + " - " + toTime;
        }
        timeSearch.setText(time);
        SearchItem();
    }
}
