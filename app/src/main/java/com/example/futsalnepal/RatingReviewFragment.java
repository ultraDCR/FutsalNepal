package com.example.futsalnepal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;


public class RatingReviewFragment extends Fragment {

    private EditText mReview;
    private Button mPost;
    private RatingBar mRating;
    public RatingReviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rating_review, container, false);

        mRating = view.findViewById(R.id.futsal_rating_input);
        mReview = view.findViewById(R.id.review_of_futsal);

        return view;
    }
}

