package com.example.futsalnepal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.futsalnepal.futsal.FutsalInfoEdit;
import com.example.futsalnepal.futsal.SpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

//https://codinginflow.com/tutorials/android/custom-dialog-interface

public class LocationDialog extends AppCompatDialogFragment {
    private Spinner pSpinner, dSpinner, vSpinner;
    private JSONObject n=null;
    private ArrayList<String> clist,dlist,vlist;
    private SpinnerAdapter dadapter;
    String distric, vdc, provienc;
    private ExampleDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.location_dialog, null);

        builder.setView(view)
                .setTitle("Location")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyTexts(provienc, distric, vdc);
                    }
                });

        pSpinner = view.findViewById(R.id.provienc_spinner);
        dSpinner = view.findViewById(R.id.district_spinner);
        vSpinner = view.findViewById(R.id.vdc_spinner);

        clist = new ArrayList<>();
        String hello = loadJSONFromAsset(getContext());

        try {
            n = new JSONObject(hello);
            Log.d("JSONFILE", "onCreate: "+n);
            clist.add(0,"-- select the province --");
            clist = findKeysOfJsonObject(n, clist);
            SpinnerAdapter adapter = new SpinnerAdapter(clist, getActivity());
            pSpinner.setAdapter(adapter);
            pSpinner.setDropDownVerticalOffset(100);

            pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //((TextView)parent.getChildAt(position)).setTextColor(Color.RED);

                    String dis = clist.get(position);

                    try {
                        dlist = new ArrayList<>();
                        dadapter = new SpinnerAdapter(dlist,getActivity());
                        dSpinner.setAdapter(dadapter);
                        dSpinner.setDropDownVerticalOffset(100);
                        dlist.clear();
                        dlist.add(0,"-- select the district --");
                        if(dis.equals("-- select the province --")){
                            dadapter.notifyDataSetChanged();
                        }else{
                            JSONObject d = n.getJSONObject(dis);
                            dlist = findKeysOfJsonObject(d, dlist);
                            dadapter.notifyDataSetChanged();
                        }




                        Log.d("LISTCHECK", "onItemSelected: "+clist);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    dlist.clear();
                    dlist.add(0,"-- select the district --");
                    dadapter.notifyDataSetChanged();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String provienc, String district, String vdc);
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
