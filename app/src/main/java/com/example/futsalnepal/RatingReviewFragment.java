package com.example.futsalnepal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RatingReviewFragment extends Fragment {

    private EditText mReview;
    private Button mPost;
    private RatingBar mRating, mRatingIndicator;
    private TextView mOverallRating,mTotalNoRating;
    private RoundCornerProgressBar mProgressOne,mProgressTwo,mProgressThree,mProgressFour,mProgressFive;
    List<User> user = fill_with_data();
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



    public RatingReviewFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_rating_review, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        FutsalIndivisualDetails activity = (FutsalIndivisualDetails) getActivity();
        String futsal_id = activity.getMyData();
        user_id = mAuth.getCurrentUser().getUid();

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

        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating != 0) {
                    filledR = true;
                    check();
                }else if(rating == 0) {
                    filledR=false;
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
                Log.d("ONCHANGE", "onTextChanged: "+s+""+start+""+before+""+count);
                if(count != 0 ){
                    filledT=true;
                    check();
                }
                else if(count == 0){
                    filledT=false;
                    check();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = (int) mRating.getRating();
                String review = mReview.getText().toString();
                number_of_rating(rating);
                total_rated_by = total_rated_by +1;
                float overalRatings =calculateOveralRating();
                Map<String, Object> ratingInfo = new HashMap<>();
                ratingInfo.put("overall_rating",overalRatings);
                Map<String, Number> starMap = new HashMap<>();
                starMap.put("one_star_rating",one_star_rating);
                starMap.put("two_star_rating",two_star_rating);
                starMap.put("three_star_rating",three_star_rating);
                starMap.put("four_star_rating",four_star_rating);
                starMap.put("five_star_rating",five_star_rating);
                starMap.put("total_rated_by", total_rated_by);
                ratingInfo.put("rating_brief_info", starMap);
                mDatabase.collection("futsal_list").document(futsal_id).update(ratingInfo);

                Map<String ,Object> rating_by = new HashMap<>();
                rating_by.put("rating", rating);
                rating_by.put("review",review);
                rating_by.put("timeStamp",FieldValue.serverTimestamp());
                mDatabase.collection("futsal_list").document(futsal_id).collection("rated_by").document(user_id).set(rating_by);
                mDatabase.collection("user_list").document(user_id).update("rated_to", FieldValue.arrayUnion(futsal_id));
                loadRating(futsal_id);
            }
        });

        RecyclerView recyclerView =  view.findViewById(R.id.review_rview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        ReviewRecyclerView adapter = new ReviewRecyclerView(user,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private float calculateOveralRating() {
        if(total_rated_by != 0){
            overall_rating = (float) ((( 5.0 * five_star_rating) + (4.0 * four_star_rating) +(3.0 * three_star_rating) +(2.0 * two_star_rating) +(1.0 * one_star_rating))/(total_rated_by));
        }
        return overall_rating;
    }

    private void loadRating(String futsal_id) {
        mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map<String, Number> week_price = (Map<String, Number>) task.getResult().get("rating_brief_info");
                    Log.d("TRating", "onComplete: "+week_price.get("one_star_rating"));
                    if(week_price.get("one_star_rating") != null){
                        one_star_rating = week_price.get("one_star_rating").floatValue();
                    }
                    if(week_price.get("two_star_rating") != null){
                        two_star_rating = week_price.get("two_star_rating").floatValue();
                    }
                    if(week_price.get("three_star_rating") != null){
                        three_star_rating = week_price.get("three_star_rating").floatValue();
                    }
                    if(week_price.get("four_star_rating") != null){
                        four_star_rating = week_price.get("four_star_rating").floatValue();
                    }
                    if(week_price.get("five_star_rating") != null){
                        five_star_rating = week_price.get("five_star_rating").floatValue();
                    }
                    if(week_price.get("total_rated_by") != null){
                        total_rated_by = week_price.get("total_rated_by").floatValue();
                    }
                    if(task.getResult().get("overall_rating") != null) {
                        Number dumy=  (Number)task.getResult().get("overall_rating");
                        overall_rating = dumy.floatValue();
                    }


                    mRatingIndicator.setRating((float) overall_rating);
                    String totalRatings = String.valueOf(total_rated_by);
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
        });
    }

    public List<User> fill_with_data() {

        List<User> user = new ArrayList<>();

        user.add(new User("Ranjan Parajuli", "20/10/2019",3));
        user.add(new User(" Deependra Dhakal", "22/11/2019",4));
        user.add(new User(" Krishna Singh", "22/11/2019",2));
        user.add(new User(" Pradeep Shrestha", "22/11/2019",5));


        return user;
    }

    //check if rating and review is filled
    public void check(){
        if(filledR && filledT){
            mPost.setBackgroundResource(R.drawable.tab_custom_shape);
            mPost.setClickable(true);
        }else {
            mPost.setBackgroundResource(R.drawable.unselected_btn_shape);
            mPost.setClickable(false);
        }
    }

    public void number_of_rating(int rating){
        switch(rating){
            case 1:
                one_star_rating++;
                break;
            case 2:
                two_star_rating ++;
                break;
            case 3:
                three_star_rating ++;
                break;
            case 4:
                four_star_rating ++;
                break;
            case 5:
                five_star_rating ++;
                break;
        }
    }

}
