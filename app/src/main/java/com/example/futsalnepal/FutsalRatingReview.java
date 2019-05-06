package com.example.futsalnepal;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.futsalnepal.Model.Review;
import com.example.futsalnepal.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalRatingReview extends Fragment {

    private RatingBar mRatingIndicator;
    private TextView mOverallRating,mTotalNoRating;
    private RoundCornerProgressBar mProgressOne,mProgressTwo,mProgressThree,mProgressFour,mProgressFive;
    private ConstraintLayout ratingLayout;
    List<User> user_list;
    List<Review> review_list;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private String futsal_id;
    private float one_star_rating = 0;
    private float two_star_rating = 0;
    private float three_star_rating = 0;
    private float four_star_rating = 0;
    private float five_star_rating = 0;
    private float total_rated_by = 0;
    float overall_rating = 0;

    public FutsalRatingReview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_rating_review, container, false);
        ConstraintLayout placeHolder = view.findViewById(R.id.include_review_rating);
        getLayoutInflater().inflate(R.layout.fragment_rating_review, placeHolder);

        Toolbar toolbar = view.findViewById(R.id.rating_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Rating & Reviews");

        ratingLayout = view.findViewById(R.id.rating_input_layout);
        ratingLayout.setVisibility(View.INVISIBLE);
        ratingLayout.setMaxHeight(0);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        mOverallRating = view.findViewById(R.id.rating_number);
        mTotalNoRating = view.findViewById(R.id.total_no_ratings);
        mProgressOne = view.findViewById(R.id.progress_for_1);
        mProgressTwo = view.findViewById(R.id.progress_for_2);
        mProgressThree = view.findViewById(R.id.progress_for_3);
        mProgressFour = view.findViewById(R.id.progress_for_4);
        mProgressFive = view.findViewById(R.id.progress_for_5);
        mRatingIndicator = view.findViewById(R.id.rating_indicater);


        if (mAuth.getCurrentUser() != null) {
            futsal_id = mAuth.getCurrentUser().getUid();
            loadRating(futsal_id);

            user_list = new ArrayList<>();
            review_list = new ArrayList<>();
            RecyclerView recyclerView = view.findViewById(R.id.review_rview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            ReviewRecyclerView adapter = new ReviewRecyclerView(user_list, review_list, getContext());
            recyclerView.setAdapter(adapter);

            mDatabase.collection("futsal_list").document(futsal_id).collection("rated_by").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {

                    for (QueryDocumentSnapshot doc : snapshots) {
                        if (doc.get("timeStamp") != null) {
                            review_list.clear();
                            user_list.clear();
                            String userId = doc.getId();
                            Review review = doc.toObject(Review.class);

                            mDatabase.collection("user_list").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        User user = task.getResult().toObject(User.class);
                                        review_list.add(review);
                                        user_list.add(user);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            });

        }
        return view;
    }
    private void loadRating(String futsal_id) {
        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().get("rating_brief_info") != null) {
                        Map<String, Number> week_price = (Map<String, Number>) task.getResult().get("rating_brief_info");
                        Log.d("TRating", "onComplete: " + week_price.get("one_star_rating"));
                        if (week_price.get("one_star_rating") != null) {
                            one_star_rating = week_price.get("one_star_rating").floatValue();
                        }
                        if (week_price.get("two_star_rating") != null) {
                            two_star_rating = week_price.get("two_star_rating").floatValue();
                        }
                        if (week_price.get("three_star_rating") != null) {
                            three_star_rating = week_price.get("three_star_rating").floatValue();
                        }
                        if (week_price.get("four_star_rating") != null) {
                            four_star_rating = week_price.get("four_star_rating").floatValue();
                        }
                        if (week_price.get("five_star_rating") != null) {
                            five_star_rating = week_price.get("five_star_rating").floatValue();
                        }
                        if (week_price.get("total_rated_by") != null) {
                            total_rated_by = week_price.get("total_rated_by").floatValue();
                        }
                        if (task.getResult().get("overall_rating") != null) {
                            Number dumy = (Number) task.getResult().get("overall_rating");
                            overall_rating = dumy.floatValue();
                        }


                        mRatingIndicator.setRating((float) overall_rating);
                        String totalRatings = String.valueOf((int) total_rated_by);
                        String ff = String.valueOf(overall_rating);
                        mOverallRating.setText(ff);
                        mTotalNoRating.setText(totalRatings);
                        mProgressOne.setMax(total_rated_by);
                        mProgressTwo.setMax(total_rated_by);
                        mProgressThree.setMax(total_rated_by);
                        mProgressFour.setMax(total_rated_by);
                        mProgressFive.setMax(total_rated_by);

                        mProgressOne.setProgress(one_star_rating);
                        mProgressTwo.setProgress(two_star_rating);
                        mProgressThree.setProgress(three_star_rating);
                        mProgressFour.setProgress(four_star_rating);
                        mProgressFive.setProgress(five_star_rating);


                    }
                }
            }
        });
    }

}