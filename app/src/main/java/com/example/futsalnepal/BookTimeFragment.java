package com.example.futsalnepal;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTimeFragment extends Fragment {
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatePickerDialog dpd;
    private RecyclerView bookTime;
    private TextView fDatePicker;
    List<BookTime> data = fill_with_data();

    public BookTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_time, container, false);

        RecyclerView recyclerView =  view.findViewById(R.id.book_time_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        BookTimeViewAdapaer adapter = new BookTimeViewAdapaer(data,getContext(),getActivity());
        recyclerView.setAdapter(adapter);

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
                        fDatePicker.setText(MONTHS[mMonth]+" "+ mDayOfMonth +","+mYear);
                    }
                },day,month,year);
                dpd.show();
            }
        });

        return view;
    }

    public List<BookTime> fill_with_data() {

        List<BookTime> data = new ArrayList<>();

        data.add(new BookTime("6am", "Kapan-3"));
        data.add(new BookTime("7am", "Chabahil"));
        data.add(new BookTime("8am", "Chabahil"));
        data.add(new BookTime("9am", "Chabahil"));
        data.add(new BookTime("10am", "Chabahil"));
        data.add(new BookTime("11am", "Chabahil"));
        data.add(new BookTime("12am", "Chabahil"));
        data.add(new BookTime("1pm", "Chabahil"));
        data.add(new BookTime("2pm", "Chabahil"));
        data.add(new BookTime("3pm", "Chabahil"));
        data.add(new BookTime("4pm", "Chabahil"));

        return data;
    }


}
