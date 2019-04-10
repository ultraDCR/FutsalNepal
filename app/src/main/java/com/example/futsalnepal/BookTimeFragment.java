package com.example.futsalnepal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookTimeFragment extends Fragment {

    private RecyclerView bookTime;
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

        BookTimeViewAdapaer adapter = new BookTimeViewAdapaer(data,getContext());
        recyclerView.setAdapter(adapter);


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
