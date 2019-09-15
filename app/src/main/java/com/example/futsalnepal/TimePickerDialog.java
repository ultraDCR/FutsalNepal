package com.example.futsalnepal;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.futsalnepal.futsal.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimePickerDialog extends DialogFragment {

    Spinner from,to;
    Button okBtn, cancelBtn,anytimeBtn;
    private SpinnerAdapter toadapter,fromadapter;
    private String to_time,from_time;
    private ArrayList<String> fromList,toList;

    static TimePickerDialog newInstance() {
        return new TimePickerDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.time_picker, container, false);

        from = dialogView.findViewById(R.id.from_spin);
        to = dialogView.findViewById(R.id.to_spin);

        fromList = new ArrayList<>();
        toList = new ArrayList<>();
        fromList = makeTimeArray();
        fromadapter = new SpinnerAdapter(fromList, getActivity());
        from.setAdapter(fromadapter);
        from.setDropDownVerticalOffset(100);
        from_time = from.getSelectedItem().toString();
        to_time = from_time;
        //toList = closeTimeArray(from_time, fromList);
        toList.add(0, "");
        toadapter = new SpinnerAdapter(toList, getActivity());
        to.setAdapter(toadapter);
        to.setDropDownVerticalOffset(100);

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from_time = fromList.get(position);
                toList.clear();
                toList = closeTimeArray(from_time, fromList);
                int index1 = findIndex(toList, to_time);
                toadapter = new SpinnerAdapter(toList, getActivity());
                to.setAdapter(toadapter);
                to.setSelection(index1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to_time = toList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelBtn = dialogView.findViewById(R.id.cancel_btn);
        okBtn = dialogView.findViewById(R.id.ok_btn);
        anytimeBtn = dialogView.findViewById(R.id.anytime_btn);
        okBtn.setOnClickListener(customDialog_UpdateOnClickListener);
        cancelBtn.setOnClickListener(customDialog_DismissOnClickListener);
         anytimeBtn.setOnClickListener(customDialog_Anytime);
        return dialogView;
    }


    private Button.OnClickListener customDialog_UpdateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            EditDialogListener activity = (EditDialogListener) getActivity();
            activity.updateResult(from_time,to_time);
            dismiss();
        }
    };

    private Button.OnClickListener customDialog_DismissOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            dismiss();
        }
    };
    private Button.OnClickListener customDialog_Anytime
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            EditDialogListener activity = (EditDialogListener) getActivity();
            activity.updateResult(null,null);
            dismiss();
        }
    };

    public interface EditDialogListener {
        void updateResult(String from,String to);
    }

    public  ArrayList<String> makeTimeArray() {

        ArrayList<String> timeArray = new ArrayList<>();
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

        return timeArray;

    }

    private ArrayList<String> closeTimeArray(String open,ArrayList<String> timeArray){
        List<String> timeArray1 = new ArrayList<>();
        int openIdx=-1;
        for(int i = 0;i < timeArray.size();i++){
            if(timeArray.get(i).equals(open)){
                openIdx = i;
            }
        }
        timeArray1 = timeArray.subList(openIdx+1, timeArray.size());
        Log.d("ARRAY4"," "+timeArray1);
        return new ArrayList<String>(timeArray1);

    }

    private int findIndex(ArrayList<String> list, String time){
        int index=-1;
        for(int i = 0; i< list.size(); i++){
            if(list.get(i).equals(time)){
                index = i;
            }
        }
        return index;
    }

}



