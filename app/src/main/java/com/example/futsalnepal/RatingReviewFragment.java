package com.example.futsalnepal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RatingReviewFragment extends Fragment {

    private EditText mReview;
    private Button mPost;
    private RatingBar mRating;
    List<User> user = fill_with_data();

    public RatingReviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rating_review, container, false);

        mRating = view.findViewById(R.id.futsal_rating_input);
        mReview = view.findViewById(R.id.review_of_futsal);

        RecyclerView recyclerView =  view.findViewById(R.id.review_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        ReviewRecyclerView adapter = new ReviewRecyclerView(user,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    public List<User> fill_with_data() {

        List<User> user = new ArrayList<>();

        user.add(new User("Ranjan Parajuli", "20/10/2019",3));
        user.add(new User(" Deependra Dhakal", "22/11/2019",4));
        user.add(new User(" Krishna Singh", "22/11/2019",2));
        user.add(new User(" Pradeep Shrestha", "22/11/2019",5));


        return user;
    }


}
