package com.example.futsalnepal;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingFutsal;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PendingRequestRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.PendingRequestRecyclerView.FutsalViewHolder>  {
    List<BookingFutsal> list;
    Context context;
    String date;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};

    public PendingRequestRecyclerView(String date, List<BookingFutsal> list, Context context) {
        this.list = list;
        this.context = context;
        this.date = date;
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
        holder.name.setText(list.get(position).futsal_name);
        holder.address.setText(list.get(position).futsal_address);
        String from_time = list.get(position).time;

        //setting from and to time in time
        int i = Arrays.asList(bookTime).indexOf(from_time);
        String to_time  = bookTime[i+1];
        Log.e("APPTEST4", "testing dates  "+from_time+"  "+i+"  "+to_time);
        holder.time.setText(from_time+" - "+to_time);


        // logic for testing date has past or not
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hha", Locale.US);
            String date3 = date+" "+list.get(position).time;
            Log.d("ERROR", "nothing parse"+date3);
            Date date1 = sdf.parse(date3);
            Log.d("ERROR", "date3 parsed: "+date1);

            Date date2 = sdf.parse(sdf.format(new Date()));
            Log.e("APPTEST1", "testing dates"+date3+"-"+date1+"-"+date2);
//        if(date1.before(date2)){
//            Log.e("app", "Date1 is before Date2");
//            return true ;
//        }
            if(date1.equals(date2) || date1.before(date2)){
                Log.e("APPTEST", "Date1 is after Date2");
                holder.layout.setBackgroundResource(R.drawable.booked_history_bg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", "onBindViewHolder: "+e);

        }

        RequestOptions placeholderRequest = new RequestOptions();
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(list.get(position).futsal_logo).into(holder.profile);

        holder.ratingBar.setRating(list.get(position).overall_rating);

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
        TextView address;
        TextView time;
        ImageView profile;
        RatingBar ratingBar;
        ConstraintLayout layout;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.p_futsal_name);
            address = itemView.findViewById(R.id.p_futsal_address);
            time = itemView.findViewById(R.id.p_futsal_available_time);
            profile = itemView.findViewById(R.id.p_futsal_profile);
            ratingBar = itemView.findViewById(R.id.p_futsal_rating);
            layout = itemView.findViewById(R.id.background_pending);

        }
    }


}