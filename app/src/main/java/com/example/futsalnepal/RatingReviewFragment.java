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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private RatingBar mRating;
    List<User> user = fill_with_data();
    private Boolean filledR = false;
    private Boolean filledT = false;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private String user_id;


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

        mRating = view.findViewById(R.id.futsal_rating_input);
        mReview = view.findViewById(R.id.review_of_futsal);
        mPost = view.findViewById(R.id.review_post_btn);

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
                String no_star = null;
                switch(rating){
                    case 1:
                        no_star ="one_star_rating";
                        break;
                    case 2:
                        no_star ="two_star_rating";
                        break;
                    case 3:
                        no_star ="three_star_rating";
                        break;
                    case 4:
                        no_star ="four_star_rating";
                        break;
                    case 5:
                        no_star ="five_star_rating";
                        break;
                }
                String finalNo_star = no_star;
                mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String, Number> week_price = (Map<String, Number>) task.getResult().get("overall_rating");
                            int stars = (int)week_price.get(finalNo_star);

                            Map<String, Object> ratingInfo = new HashMap<>();
                            Map<String, Number> starMap = new HashMap<>();
                            starMap.put(finalNo_star,stars++);
                            ratingInfo.put("fourstarrating", starMap);
                            mDatabase.collection("futsal_list").document(futsal_id).update(ratingInfo);
                        }
                    }
                });




                mDatabase.collection("futsal_list").document(futsal_id).update("overall_rating",FieldValue.increment(rating));
                mDatabase.collection("user_list").document(user_id).update("rated_to", FieldValue.arrayUnion(futsal_id));

            }
        });

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

}
