package com.example.futsalnepal.futsal;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
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

public class FutsalBookedRecyclerView extends RecyclerView.Adapter<FutsalBookedRecyclerView.FutsalViewHolder>  {
    List<BookingUser> list;
    Context context;
    String date;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};

    public FutsalBookedRecyclerView(String date, List<BookingUser> list, Context context) {
        this.list = list;
        this.context = context;
        this.date = date;
    }

    @Override
    public FutsalBookedRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_user_single_layout, parent, false);
        FutsalBookedRecyclerView.FutsalViewHolder holder = new FutsalBookedRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(FutsalBookedRecyclerView.FutsalViewHolder holder, int position) {

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

        holder.layout.setOnClickListener(view ->{
            Intent userProfile = new Intent(context, UserProfile.class);
            userProfile.putExtra("user_id",list.get(position).user_id);
            context.startActivity(userProfile);

        });
        //animate(holder);


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

//    @Override
//    public Filter getFilter() {
//        return dateFilter;
//    }
//
//    private Filter dateFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Futsal> filterList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0){
//                filterList.addAll(filter);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(Futsal item : filter){
//                    if(   )
//                }
//            }
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    };

    public class FutsalViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView time;
        TextView phone;
        ImageView profile;
        ConstraintLayout layout;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.a_person_name_);
            date = itemView.findViewById(R.id.a_book_date);
            time = itemView.findViewById(R.id.a_book_time);
            profile = itemView.findViewById(R.id.a_profile_pic);
            phone = itemView.findViewById(R.id.a_book_phone);
            layout = itemView.findViewById(R.id.br_background);

        }
    }


}