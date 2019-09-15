package com.example.futsalnepal.users;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingFutsal;
import com.example.futsalnepal.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HistoryRecyclerView extends RecyclerView.Adapter<HistoryRecyclerView.FutsalViewHolder>{
    List<BookingFutsal> list = Collections.emptyList();
    Context context;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};


    public HistoryRecyclerView(List<BookingFutsal> list, Context context) {
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

        holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.cv.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).futsal_name);
        Map<String,Object> futsal_address = list.get(position).location;
        String location =futsal_address.get("vdc").toString()+", "+futsal_address.get("district").toString();
        holder.address.setText(location);
        holder.time.setText(list.get(position).futsal_phone);

        String from_time = list.get(position).time;
        //setting from and to time in time
        int i = Arrays.asList(bookTime).indexOf(from_time);
        String to_time  = bookTime[i+1];
        Log.e("APPTEST4", "testing dates  "+from_time+"  "+i+"  "+to_time);
        holder.time.setText(from_time+" - "+to_time);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.futsal_time_logo);
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
    public void insert(int position, BookingFutsal data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookingFutsal data) {
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
        CardView cv;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.bh_futsal_name);
            address =  itemView.findViewById(R.id.bh_futsal_address);
            time =  itemView.findViewById(R.id.bh_futsal_available_time);
            profile =  itemView.findViewById(R.id.bh_futsal_profile);
            ratingBar = itemView.findViewById(R.id.bh_futsal_rating);
            cv = itemView.findViewById(R.id.futsal_card_view);

        }
    }
}
