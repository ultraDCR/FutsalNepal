package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.futsalnepal.Model.Data;

import java.util.Collections;
import java.util.List;

public class PendingRequestRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.PendingRequestRecyclerView.FutsalViewHolder> {
    List<Data> list = Collections.emptyList();
    Context context;

    public PendingRequestRecyclerView(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PendingRequestRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_layout, parent, false);
        PendingRequestRecyclerView.FutsalViewHolder holder = new PendingRequestRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(PendingRequestRecyclerView.FutsalViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).name);
        holder.address.setText(list.get(position).address);
        holder.time.setText(list.get(position).time);
        holder.profile.setImageResource(list.get(position).imageId);
        holder.ratingBar.setRating(list.get(position).rating);

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
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
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
            name = itemView.findViewById(R.id.p_futsal_name);
            address = itemView.findViewById(R.id.p_futsal_address);
            time = itemView.findViewById(R.id.p_futsal_available_time);
            profile = itemView.findViewById(R.id.p_futsal_profile);
            ratingBar = itemView.findViewById(R.id.p_futsal_rating);

        }
    }
}