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
public class BookedFragment extends Fragment {

    List<Data> data = fill_with_data();

    public BookedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booked, container, false);


        RecyclerView recyclerView =  view.findViewById(R.id.booked_rview);
        BookedRecyclerView adapter = new BookedRecyclerView(data, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        return view;
    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("WhiteHouse", "Kapan-3","6AM - 6PM", R.mipmap.ic_futsal_foreground,4));
        data.add(new Data("BlackHouses", "Chabahil","9AM - 9PM", R.drawable.logo,2));

        return data;
    }

}
