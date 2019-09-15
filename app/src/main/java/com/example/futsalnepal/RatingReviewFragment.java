package com.example.futsalnepal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.futsalnepal.Model.Review;
import com.example.futsalnepal.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RatingReviewFragment extends Fragment {

    private EditText mReview;
    private Button mPost;
    private RatingBar mRating, mRatingIndicator;
    private TextView mOverallRating, mTotalNoRating;
    private RoundCornerProgressBar mProgressOne, mProgressTwo, mProgressThree, mProgressFour, mProgressFive;
    private ConstraintLayout ratingLayout;
    List<User> users_list;
    List<Review> review_list;
    private Boolean filledR = false;
    private Boolean filledT = false;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private String user_id;
    private float one_star_rating = 0;
    private float two_star_rating = 0;
    private float three_star_rating = 0;
    private float four_star_rating = 0;
    private float five_star_rating = 0;
    private float total_rated_by = 0;
    float overall_rating = 0;
    private static DecimalFormat df2 = new DecimalFormat("#.#");


    public RatingReviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating_review, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        FutsalIndivisualDetails activity = (FutsalIndivisualDetails) getActivity();
        String futsal_id = activity.getMyData();

        ratingLayout = view.findViewById(R.id.rating_input_layout);
        mRating = view.findViewById(R.id.futsal_rating_input);
        mReview = view.findViewById(R.id.review_of_futsal);
        mPost = view.findViewById(R.id.review_post_btn);
        mOverallRating = view.findViewById(R.id.rating_number);
        mTotalNoRating = view.findViewById(R.id.total_no_ratings);
        mProgressOne = view.findViewById(R.id.progress_for_1);
        mProgressTwo = view.findViewById(R.id.progress_for_2);
        mProgressThree = view.findViewById(R.id.progress_for_3);
        mProgressFour = view.findViewById(R.id.progress_for_4);
        mProgressFive = view.findViewById(R.id.progress_for_5);
        mRatingIndicator = view.findViewById(R.id.rating_indicater);

        loadRating(futsal_id);
        validateRatingInput();
        if (mAuth.getCurrentUser() != null) {
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("futsal_list").document(futsal_id).collection("rated_by").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            ratingLayout.setVisibility(View.GONE);
                        }
                    }
                }
            });

        }
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    ratingLayout.setVisibility(View.GONE);
                    int rating = (int) mRating.getRating();
                    String review = mReview.getText().toString();
                    number_of_rating(rating);
                    total_rated_by = total_rated_by + 1;
                    float overalRatings = calculateOveralRating();
                    Log.d("TTE", "onClick: " + overalRatings);
                    Map<String, Object> ratingInfo = new HashMap<>();
                    ratingInfo.put("overall_rating", overalRatings);
                    Map<String, Number> starMap = new HashMap<>();
                    starMap.put("one_star_rating", one_star_rating);
                    starMap.put("two_star_rating", two_star_rating);
                    starMap.put("three_star_rating", three_star_rating);
                    starMap.put("four_star_rating", four_star_rating);
                    starMap.put("five_star_rating", five_star_rating);
                    starMap.put("total_rated_by", total_rated_by);
                    ratingInfo.put("rating_brief_info", starMap);
                    mDatabase.collection("futsal_list").document(futsal_id).update(ratingInfo);

                    Map<String, Object> rating_by = new HashMap<>();
                    rating_by.put("rating", rating);
                    rating_by.put("review", review);
                    rating_by.put("timeStamp", FieldValue.serverTimestamp());
                    mDatabase.collection("futsal_list").document(futsal_id).collection("rated_by")
                            .document(user_id).set(rating_by)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loadRating(futsal_id);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    ratingLayout.setVisibility(View.VISIBLE);
                                }
                            });

                    mDatabase.collection("users_list").document(user_id).update("rated_to", FieldValue.arrayUnion(futsal_id));

                } else {
                    DialogFragment newFragment = new LoginAndSignUp();
                    newFragment.show(getFragmentManager(), "rating & review");
                }
            }
        });


        users_list = new ArrayList<>();
        review_list = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.review_rview);
        recyclerView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.recyclerview));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ReviewRecyclerView adapter = new ReviewRecyclerView(users_list, review_list, getContext());
        recyclerView.setAdapter(adapter);

        mDatabase.collection("futsal_list").document(futsal_id).collection("rated_by").
                orderBy("timeStamp", Query.Direction.ASCENDING).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {

                        for (QueryDocumentSnapshot doc : snapshots) {
                            if (doc.get("timeStamp") != null) {

                                String userId = doc.getId();

                                review_list.clear();
                                users_list.clear();
                                mDatabase.collection("users_list").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            Review review = doc.toObject(Review.class);
                                            User user = task.getResult().toObject(User.class);
                                            review_list.add(review);
                                            users_list.add(user);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });


        return view;
    }

    private void validateRatingInput() {
        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating != 0) {
                    filledR = true;
                    check();
                } else if (rating == 0) {
                    filledR = false;
                    check();
                }

            }
        });

        mReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ONCHANGE", "onTextChanged: " + s + "" + start + "" + before + "" + count);
                if (count != 0) {
                    filledT = true;
                    check();
                } else if (count == 0 && start == 0) {
                    filledT = false;
                    check();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private float calculateOveralRating() {
        if (total_rated_by != 0) {
            double Rating = (((5.0 * five_star_rating) + (4.0 * four_star_rating) + (3.0 * three_star_rating) + (2.0 * two_star_rating) + (1.0 * one_star_rating)) / (total_rated_by));
            overall_rating = Float.parseFloat(df2.format(Rating));
            Log.d("RTT", "calculateOveralRating: " + overall_rating);
        }
        return overall_rating;
    }

    private void loadRating(String futsal_id) {
        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
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

    //check if rating and review is filled
    public void check() {
        if (filledR && filledT) {
            mPost.setBackgroundResource(R.drawable.tab_custom_shape);
            mPost.setClickable(true);
        } else {
            mPost.setBackgroundResource(R.drawable.unselected_btn_shape);
            mPost.setClickable(false);
        }
    }

    public void number_of_rating(int rating) {
        switch (rating) {
            case 1:
                one_star_rating++;
                break;
            case 2:
                two_star_rating++;
                break;
            case 3:
                three_star_rating++;
                break;
            case 4:
                four_star_rating++;
                break;
            case 5:
                five_star_rating++;
                break;
        }
    }


}
