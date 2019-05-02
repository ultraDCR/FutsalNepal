package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.Data;
import com.example.futsalnepal.Model.Futsal;

import java.util.Collections;
import java.util.List;

public class HistoryRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.HistoryRecyclerView.FutsalViewHolder>{
    List<Futsal> list = Collections.emptyList();
    Context context;

    public HistoryRecyclerView(List<Futsal> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public HistoryRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_history_layout, parent, false);
        HistoryRecyclerView.FutsalViewHolder holder = new HistoryRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(HistoryRecyclerView.FutsalViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).futsal_name);
        holder.address.setText(list.get(position).futsal_address);
        holder.time.setText(list.get(position).futsal_phone);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.logo);
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(list.get(position).futsal_logo).into(holder.profile);

        holder.ratingBar.setRating(list.get(position).overall_rating);

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
    public void insert(int position, Futsal data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Futsal data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class FutsalViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView time;
        ImageView profile;
        RatingBar ratingBar;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.bh_futsal_name);
            address =  itemView.findViewById(R.id.bh_futsal_address);
            time =  itemView.findViewById(R.id.bh_futsal_available_time);
            profile =  itemView.findViewById(R.id.bh_futsal_profile);
            ratingBar = itemView.findViewById(R.id.bh_futsal_rating);

        }
    }
}
