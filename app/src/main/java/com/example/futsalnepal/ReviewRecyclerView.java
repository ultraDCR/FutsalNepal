package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.Review;
import com.example.futsalnepal.Model.User;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReviewRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder> {

    List<User> user_list ;
    List<Review> review_list;
    Context context;

    public ReviewRecyclerView(List<User> user_list,List<Review> review_list, Context context) {
        this.user_list = user_list;
        this.review_list = review_list;
        this.context = context;
    }

    @Override
    public com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder holder = new com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView

        String user_name = user_list.get(position).getUser_full_name();
        String user_profile = user_list.get(position).getUser_profile_image();

        holder.setUserInfo(user_name, user_profile);

        long millisecond = review_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        float rating  = review_list.get(position).getRating().floatValue();
        String review =  review_list.get(position).getReview();

        holder.setFutsalReview(dateString,rating,review);
        //animate(holder);


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return review_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Review review) {
        review_list.add(position, review);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Review review) {
        int position = review_list.indexOf(review);
        review_list.remove(position);
        notifyItemRemoved(position);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {


        TextView name,time,reviewText;
        CircleImageView profile;
        RatingBar rating;
        View mView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            name = itemView.findViewById(R.id.review_user_name);
            profile = itemView.findViewById(R.id.review_user_pp);
            time = itemView.findViewById(R.id.review_date);
            rating = itemView.findViewById(R.id.rating_bar);
            reviewText = itemView.findViewById(R.id.review_text);
        }

        public void setUserInfo(String user_name, String profile_pic){
            name.setText(user_name);

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.unselected_btn_shape);
            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(profile_pic).into(profile);
        }

        public void setFutsalReview(String timestamp,float ratingNo, String review){
            rating.setRating(ratingNo);
            time.setText(timestamp);
            reviewText.setText(review);
        }
    }

}