package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class ReviewRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.ReviewRecyclerView.ReviewViewHolder> {

    List<User> list = Collections.emptyList();
    Context context;

    public ReviewRecyclerView(List<User> list, Context context) {
        this.list = list;
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
        holder.name.setText(list.get(position).name);
        holder.time.setText(list.get(position).time);
        holder.rating.setRating(list.get(position).rating);

        //animate(holder);


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, User user) {
        list.add(position, user);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(User user) {
        int position = list.indexOf(user);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {


        TextView name,time;
       RatingBar rating;


        ReviewViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.review_user_name);
            time = itemView.findViewById(R.id.review_date);
            rating = itemView.findViewById(R.id.rating_bar);


        }
    }

}