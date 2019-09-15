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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FutsalNewRequestRecyclerView extends RecyclerView.Adapter<FutsalNewRequestRecyclerView.FutsalViewHolder>  {
    List<BookingUser> list;
    Context context;
    String date;
    String futsal_id;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};

    public FutsalNewRequestRecyclerView(String date, List<BookingUser> list, Context context) {
        this.list = list;
        this.context = context;
        this.date = date;
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    public FutsalNewRequestRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_request_user_single_layout, parent, false);
        FutsalNewRequestRecyclerView.FutsalViewHolder holder = new FutsalNewRequestRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(FutsalNewRequestRecyclerView.FutsalViewHolder holder, int position) {

        holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        futsal_id = mauth.getCurrentUser().getUid();

        holder.name.setText(list.get(position).user_full_name);
        holder.date.setText(date);
        holder.phone.setText(list.get(position).user_phone_number);
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
                holder.layout.setBackgroundResource(R.drawable.user_history_bg);
                holder.denyBtn.setClickable(false);
                holder.denyBtn.setVisibility(View.INVISIBLE);
                holder.acceptBtn.setClickable(false);
                holder.acceptBtn.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", "onBindViewHolder: "+e);

        }

        RequestOptions placeholderRequest = new RequestOptions();
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(list.get(position).user_profile_image).into(holder.profile);

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Accept booking for "+date+" "+list.get(position).time +" ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                addToDatabase(list, position);
                                removeFromDatabase(list, position);
                                addNotificatin(list,position,"accepted");
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

            }
        });

        holder.denyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Reject booking for "+date+" "+list.get(position).time +" ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                removeFromDatabase(list, position);
                                addNotificatin(list,position,"rejected");
//                              notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();


        }
        });

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
        Button acceptBtn,denyBtn;
        //RatingBar ratingBar;
        ConstraintLayout layout;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.br_person_name_);
            date = itemView.findViewById(R.id.br_book_date);
            time = itemView.findViewById(R.id.br_book_time);
            profile = itemView.findViewById(R.id.br_circleView);
            phone = itemView.findViewById(R.id.br_book_phone);
            layout = itemView.findViewById(R.id.br_background);
            acceptBtn = itemView.findViewById(R.id.accept_btn);
            denyBtn = itemView.findViewById(R.id.reject_btn);

        }
    }


    private void addToDatabase(List<BookingUser> list, int position){
        String user_id = list.get(position).user_id;
        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> timeMap = new HashMap<>();
        timeMap.put(list.get(position).time, FieldValue.serverTimestamp());
        userMap.put(futsal_id, timeMap);


        Map<String, Object> futsalMap = new HashMap<>();
        Map<String, Object> timeMap1 = new HashMap<>();
        timeMap1.put(list.get(position).time, FieldValue.serverTimestamp());
        futsalMap.put(user_id, timeMap1);


        mDatabase.collection("futsal_list").document(futsal_id)
                .collection("booked").document(date).set(futsalMap, SetOptions.merge());
        mDatabase.collection("users_list").document(user_id)
                .collection("booked").document(date).set(userMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyItemRemoved(position);
            }
        });

    }

    private void removeFromDatabase(List<BookingUser> list, int position) {
        String user_id = list.get(position).user_id;

        Map<String, Object> userMap = new HashMap<>();
        Map<String, Object> timeMap = new HashMap<>();
        timeMap.put(list.get(position).time, FieldValue.delete());
        userMap.put(futsal_id, timeMap);


        Map<String, Object> futsalMap = new HashMap<>();
        Map<String, Object> timeMap1 = new HashMap<>();
        timeMap1.put(list.get(position).time, FieldValue.delete());
        futsalMap.put(user_id, timeMap1);


        mDatabase.collection("users_list").document(user_id)
                .collection("pending").document(date).set(userMap, SetOptions.merge());
        mDatabase.collection("futsal_list").document(futsal_id)
                .collection("newrequest").document(date).set(futsalMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyItemRemoved(position);
            }
        });


    }

    private void addNotificatin(List<BookingUser> list, int position, String type) {
        String user_id = list.get(position).user_id;
        String message = "Book request on "+date+" at "+list.get(position).time+" was "+type;
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("from", futsal_id);
        notificationMap.put("type", type);
        notificationMap.put("message", message);
        notificationMap.put("status","notseen");
        notificationMap.put("timestamp",FieldValue.serverTimestamp());

        mDatabase.collection("users_list").document(user_id)
                .collection("Notification").add(notificationMap);

    }

}

