package com.example.futsalnepal;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalRatingReview extends Fragment {


    public FutsalRatingReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_futsal_rating_review, container, false);
        ConstraintLayout placeHolder =  view.findViewById(R.id.include_review_rating);
        getLayoutInflater().inflate(R.layout.fragment_rating_review, placeHolder);

        Toolbar toolbar= view.findViewById(R.id.rating_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setTitle("Rating & Reviews");

        return view;
    }

}
