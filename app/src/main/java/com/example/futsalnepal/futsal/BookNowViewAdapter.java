package com.example.futsalnepal.futsal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookTime;
import com.example.futsalnepal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class BookNowViewAdapter extends RecyclerView.Adapter<BookNowViewAdapter.BookTimeViewHolder> {

    List<BookTime> list ;
    Context context;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    Activity activity;
    String date;
    String futsal_id;


    public BookNowViewAdapter(List<BookTime> list, String date, Context context, Activity activity) {
        this.list = list;
        this.date = date;
        Log.d("FUTSAL", "BookNowViewAdapter: "+futsal_id);
        this.context = context;
        this.activity = activity;
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    public BookTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_time_single_layout, parent, false);
        BookTimeViewHolder holder = new BookTimeViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(BookTimeViewHolder holder, int position) {

        holder.book_time.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.bookBtn.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));

        mauth = FirebaseAuth.getInstance();
        futsal_id = mauth.getCurrentUser().getUid();

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.book_time.setText(list.get(position).book_time);
        Log.d("ARRAY4", "onBindViewHolder: "+futsal_id+"  "+date);
        boolean pastTime = holder.pastTimeDisable(list.get(position).book_time, date);
        if(pastTime) {
            holder.firstLoadPendingData(list.get(position).book_time);

            holder.bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.status == "normal") {
                        Map<String, Object> futsalMap = new HashMap<>();
                        Map<String, Object> timeMap1 = new HashMap<>();
                        timeMap1.put(list.get(position).book_time, FieldValue.serverTimestamp());
                        futsalMap.put(futsal_id, timeMap1);

                        new AlertDialog.Builder(context)
                                .setMessage("Do you want to booking your futsal at " + list.get(position).book_time + " ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabase.collection("futsal_list").document(futsal_id)
                                                .collection("booked").document(date).set(futsalMap, SetOptions.merge());
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();


                    }
                    else if (holder.status == "booked") {
                        Map<String, Object> futsalMap = new HashMap<>();
                        Map<String, Object> timeMap1 = new HashMap<>();
                        timeMap1.put(list.get(position).book_time, FieldValue.delete());
                        futsalMap.put(holder.id, timeMap1);

                        Map<String, Object> user = new HashMap<>();
                        Map<String, Object> usertime = new HashMap<>();
                        usertime.put(list.get(position).book_time, FieldValue.delete());
                        user.put(holder.id, timeMap1);

                        String message = "Booking request received on "+date+" at "+list.get(position).book_time+"has been canceled. Please call us for more information.";
                        Map<String, Object> notificationMap = new HashMap<>();
                        notificationMap.put("from", futsal_id);
                        notificationMap.put("type", "cancled");
                        notificationMap.put("message", message);
                        notificationMap.put("status","notseen");
                        notificationMap.put("timestamp",FieldValue.serverTimestamp());

                        new AlertDialog.Builder(context)
                                .setMessage("Do you want to calcel booking at " + list.get(position).book_time + " ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        mDatabase.collection("futsal_list").document(futsal_id)
                                                .collection("booked").document(date).set(futsalMap, SetOptions.merge())
                                                .addOnSuccessListener(aVoid -> {
                                                    holder.cancelbooking();
                                                });
                                        if(holder.id != futsal_id){
                                            mDatabase.collection("users_list").document(holder.id)
                                                    .collection("booked").document(date).set(user, SetOptions.merge());
                                            mDatabase.collection("users_list").document(holder.id)
                                                    .collection("Notification").add(notificationMap);

                                        }
                                        //holder.firstLoadPendingData(list.get(position).book_time);
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();


                    }
                }

            });


        }

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    //this two method fix multiple holder with same item id.
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, BookTime data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookTime data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class BookTimeViewHolder extends RecyclerView.ViewHolder {


        TextView book_time;
        Button bookBtn;
        String status = "normal";
        String id;



        BookTimeViewHolder(View itemView) {
            super(itemView);
            book_time =  itemView.findViewById(R.id.book_time);
            bookBtn =  itemView.findViewById(R.id.book_time_btn);


        }

        public void setBooked() {
            bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
            bookBtn.setBackgroundResource(R.drawable.your_booking_button);
            bookBtn.setText("Booked");
            bookBtn.setClickable(false);
        }
        public void cancelbooking(){
            bookBtn.setTextColor(Color.parseColor("#5FBA3A"));
            bookBtn.setBackgroundResource(R.drawable.green_strok_button);
            bookBtn.setText("Book Now");
            bookBtn.setClickable(true);
            status = "normal";

        }
        public void firstLoadPendingData(String bookdate){
            firstLoadBookedData(bookdate);
            mDatabase.collection("futsal_list").document(futsal_id)
                    .collection("newrequest").document(date).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot != null){
                        Log.d("CLICKABLE", "onEvent: "+documentSnapshot.getData());
                        Map<String, Object> userIdMap = documentSnapshot.getData();
                        if(userIdMap != null) {
                            for (String user_id : userIdMap.keySet()) {
                                Log.d("CLICKABLE", "onEvent: " + user_id);
                                Map<String, Object> timeMap = (Map<String, Object>) userIdMap.get(user_id);
                                Log.d("CLICKABLE", "map: " + timeMap + "__" + timeMap.get(bookdate) + "__" + bookdate);
                                if (timeMap.get(bookdate) != null) {
                                    bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                    bookBtn.setBackgroundResource(R.drawable.pending_button);
                                    bookBtn.setText("Pending");
                                    bookBtn.setClickable(false);

                                }
                                firstLoadBookedData(bookdate);

                            }

                        }
                    }
                }
            });

        }

        public void firstLoadBookedData(String book_time) {

            mDatabase.collection("futsal_list").document(futsal_id)
                    .collection("booked").document(date).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot != null){
                        Map<String, Object> userIdMap = documentSnapshot.getData();
                        if(userIdMap != null) {
                            for (String user_id : userIdMap.keySet()) {
                            Map<String, Object> timeMap = (Map<String, Object>) userIdMap.get(user_id);
                                if (timeMap.get(book_time) != null) {
                                    bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                    bookBtn.setBackgroundResource(R.drawable.already_booked_button);
                                    bookBtn.setText("Already Booked");
                                    id = user_id;
                                    status = "booked";
                                    //bookBtn.setClickable(false);
                                    if (mauth.getCurrentUser() != null) {
                                        String uid = mauth.getCurrentUser().getUid();
                                        Log.d("MAP2", "onComplete: " + uid + "      " + user_id);
                                        if (user_id.equals(uid)) {
                                            bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                            bookBtn.setBackgroundResource(R.drawable.your_booking_button);
                                            bookBtn.setText("Booked");
//                                          bookBtn.setClickable(false);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

        public Boolean pastTimeDisable(String book_time, String date) {
            String Time[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};
            SimpleDateFormat sdf = new SimpleDateFormat("ha", Locale.US);
            String currentTime = sdf.format(new Date());

            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            String currentDate = sdf1.format(new Date());
            //String currentDate = DateFormat.getDateInstance().format(new Date());

            int timeIndex = Arrays.asList(Time).indexOf(currentTime);
            int bookIndex = Arrays.asList(Time).indexOf(book_time);
            Log.d("TIME_TEST3", "pastTimeDisable: "+currentTime+"  "+currentDate);
            Log.d("TIME_TEST3", "pastTimeDisable: "+timeIndex+"  "+bookIndex);
            Log.d("TIME_TEST1", "pastTimeDisable: "+currentDate+"  "+date);
            Log.d("TIME_TEST2", "pastTimeDisable: "+(bookIndex <= timeIndex )+"  -"+(currentDate.equals(date)));

            if(bookIndex <= timeIndex && currentDate.equals(date)){
                bookBtn.setTextColor(Color.parseColor("#BFF5AE"));
                bookBtn.setBackgroundResource(R.drawable.past_time_button);
                bookBtn.setClickable(false);
                boolean f = bookBtn.isClickable();
                Log.d("TIME_TEST", "pastTimeDisable: "+f);
                return false;
            }
            return true;
        }
    }

}

