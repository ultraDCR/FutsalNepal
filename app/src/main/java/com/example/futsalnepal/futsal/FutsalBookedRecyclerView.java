package com.example.futsalnepal.futsal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingUser;
import com.example.futsalnepal.R;
import com.example.futsalnepal.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FutsalBookedRecyclerView extends RecyclerView.Adapter<FutsalBookedRecyclerView.FutsalViewHolder>  {
    List<BookingUser> list;
    Context context;
    FirebaseAuth mAuth;
    FirebaseFirestore mdatabase;
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

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseFirestore.getInstance();
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

        holder.cancel.setOnClickListener(v ->{
            String futsal_id = mAuth.getCurrentUser().getUid();

            Map<String, Object> futsalMap = new HashMap<>();
            Map<String, Object> timeMap1 = new HashMap<>();
            timeMap1.put(from_time, FieldValue.delete());
            futsalMap.put(list.get(position).user_id, timeMap1);

            Map<String, Object> user = new HashMap<>();
            Map<String, Object> usertime = new HashMap<>();
            usertime.put(list.get(position).time, FieldValue.delete());
            user.put(futsal_id, timeMap1);

            String message = "Booking request received on "+date+" at "+list.get(position).time+"has been canceled. Please call us for more information.";
            Map<String, Object> notificationMap = new HashMap<>();
            notificationMap.put("from", futsal_id);
            notificationMap.put("type", "cancled");
            notificationMap.put("message", message);
            notificationMap.put("status","notseen");
            notificationMap.put("timestamp",FieldValue.serverTimestamp());

            new AlertDialog.Builder(context)
                    .setMessage("Do you want to calcel booking at " + list.get(position).time + " ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mdatabase.collection("futsal_list").document(futsal_id)
                                    .collection("booked").document(date).set(futsalMap, SetOptions.merge());

                            mdatabase.collection("users_list").document(list.get(position).user_id)
                                    .collection("booked").document(date).set(user, SetOptions.merge());
                            mdatabase.collection("users_list").document(list.get(position).user_id)
                                    .collection("Notification").add(notificationMap);


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
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
        Button cancel;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.a_person_name_);
            date = itemView.findViewById(R.id.a_book_date);
            time = itemView.findViewById(R.id.a_book_time);
            profile = itemView.findViewById(R.id.a_profile_pic);
            phone = itemView.findViewById(R.id.a_book_phone);
            layout = itemView.findViewById(R.id.br_background);
            cancel = itemView.findViewById(R.id.cancel_book);

        }
    }


}