package com.example.futsalnepal.futsal;

import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingUser;
import com.example.futsalnepal.R;
import com.example.futsalnepal.UserProfile;

import java.util.Arrays;
import java.util.List;

public class FutsalHistoryRecyclerView extends RecyclerView.Adapter<FutsalHistoryRecyclerView.FutsalViewHolder>  {
    List<BookingUser> list;
    Context context;
    String date;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};

    public FutsalHistoryRecyclerView(String date, List<BookingUser> list, Context context) {
        this.list = list;
        this.context = context;
        this.date = date;
    }

    @Override
    public FutsalHistoryRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_user_single_layout, parent, false);
        FutsalHistoryRecyclerView.FutsalViewHolder holder = new FutsalHistoryRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(FutsalHistoryRecyclerView.FutsalViewHolder holder, int position) {


        holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).user_full_name);
        holder.date.setText(date);
        holder.phone.setText(list.get(position).user_phone_number);
        String from_time = list.get(position).time;

        //setting from and to time in time
        int i = Arrays.asList(bookTime).indexOf(from_time);
        String to_time  = bookTime[i+1];
        Log.e("APPTEST4", "testing dates  "+from_time+"  "+i+"  "+to_time);
        holder.time.setText(from_time+" - "+to_time);


        RequestOptions placeholderRequest = new RequestOptions();
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(list.get(position).user_profile_image).into(holder.profile);

        //animate(holder);

        holder.layout.setOnClickListener(view ->{
            Intent userProfile = new Intent(context, UserProfile.class);
            userProfile.putExtra("user_id",list.get(position).user_id);
            context.startActivity(userProfile);

        });


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        Log.d("DATETEST8", "getItemCount: "+list);
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, BookingUser data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookingUser data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


    public class FutsalViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView time;
        TextView phone;
        ImageView profile;
        //RatingBar ratingBar;
        ConstraintLayout layout;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.h_person_name_);
            date = itemView.findViewById(R.id.h_book_date);
            time = itemView.findViewById(R.id.h_book_time);
            profile = itemView.findViewById(R.id.h_circleView);
            phone = itemView.findViewById(R.id.h_book_phone);
            layout = itemView.findViewById(R.id.h_background);

        }
    }


}