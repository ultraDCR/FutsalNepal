package com.example.futsalnepal;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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
import com.google.android.gms.tasks.OnSuccessListener;
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


public class BookTimeViewAdapter extends RecyclerView.Adapter<BookTimeViewAdapter.BookTimeViewHolder> {

    List<BookTime> list ;
    Context context;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    Fragment activity;
    String date;
    String futsal_id;
    DialogFragment newFragment;

    public BookTimeViewAdapter(List<BookTime> list, String date, String futsal_id, Context context, Fragment activity) {
        this.list = list;
        this.date = date;
        this.futsal_id = futsal_id;
        Log.d("FUTSAL", "BookTimeViewAdapter: "+futsal_id);
        this.context = context;
        this.activity = activity;
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        newFragment = new LoginAndSignUp();
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
        mauth = FirebaseAuth.getInstance();

        holder.book_time.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.bookBtn.setAnimation(AnimationUtils.loadAnimation(context,R.anim.book_time_animation));
        holder.pastTimeLayout .setAnimation(AnimationUtils.loadAnimation(context,R.anim.book_time_animation));


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.book_time.setText(list.get(position).book_time);
        Log.d("ARRAY4", "onBindViewHolder: "+futsal_id+"  "+date);
        boolean pastTime = holder.pastTimeDisable(list.get(position).book_time, date);
        holder.firstLoadPendingData(list.get(position).book_time,pastTime);
        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICKABLE", "onBindViewHolder: "+holder.own_pending);
                if (mauth.getCurrentUser() == null) {
                    newFragment.show(activity.getFragmentManager(),"dd");
                    Log.d("pressed", "alertdialog");
                }else {
                    if (holder.own_pending) {
                        Log.d("CLICKABLE1", "onBindViewHolder: " + holder.own_pending);
                        String user_id = mauth.getCurrentUser().getUid();

                        Map<String, Object> userMap = new HashMap<>();
                        Map<String, Object> timeMap = new HashMap<>();
                        timeMap.put(list.get(position).book_time, FieldValue.delete());
                        userMap.put(futsal_id, timeMap);


                        Map<String, Object> futsalMap = new HashMap<>();
                        Map<String, Object> timeMap1 = new HashMap<>();
                        timeMap1.put(list.get(position).book_time, FieldValue.delete());
                        futsalMap.put(user_id, timeMap1);

                        String message = "Booking request for "+date+" at "+list.get(position).book_time+"was cancled";
                        Map<String, Object> notificationMap = new HashMap<>();
                        notificationMap.put("from", user_id);
                        notificationMap.put("type", "removed");
                        notificationMap.put("message", message);
                        notificationMap.put("status","notseen");
                        notificationMap.put("timestamp",FieldValue.serverTimestamp());

                        new AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to cancle booking request of"+list.get(position).book_time +" ?")
                                .setPositiveButton("YES", (dialog, which) -> {
                                    mDatabase.collection("users_list").document(user_id)
                                            .collection("pending").document(date).set(userMap, SetOptions.merge());
                                    mDatabase.collection("futsal_list").document(futsal_id)
                                            .collection("newrequest").document(date).set(futsalMap, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    holder.cancelbooking();
                                                }
                                            });
                                    mDatabase.collection("futsal_list").document(futsal_id)
                                            .collection("Notification").add(notificationMap);

                                })
                                .setNegativeButton("NO", (dialog, which) -> {
                                    //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                                })
                                .show();

                    } else {
                        String user_id = mauth.getCurrentUser().getUid();
                        Log.d("CLICKABLE1", "onBindViewHolder: " + user_id);
                        Map<String, Object> userMap = new HashMap<>();
                        Map<String, Object> timeMap = new HashMap<>();
                        timeMap.put(list.get(position).book_time, FieldValue.serverTimestamp());
                        userMap.put(futsal_id, timeMap);


                        Map<String, Object> futsalMap = new HashMap<>();
                        Map<String, Object> timeMap1 = new HashMap<>();
                        timeMap1.put(list.get(position).book_time, FieldValue.serverTimestamp());
                        futsalMap.put(user_id, timeMap1);

                        String message = "You have new booking request for "+date+" at "+list.get(position).book_time;
                        Map<String, Object> notificationMap = new HashMap<>();
                        notificationMap.put("from", user_id);
                        notificationMap.put("type", "added");
                        notificationMap.put("message", message);
                        notificationMap.put("status","notseen");
                        notificationMap.put("timestamp",FieldValue.serverTimestamp());


                        new AlertDialog.Builder(context)
                                .setMessage("Do you want to book the futsal at "+list.get(position).book_time +" ?")
                                .setPositiveButton("YES", (dialog, which) -> {
                                    mDatabase.collection("futsal_list").document(futsal_id)
                                            .collection("newrequest").document(date).set(futsalMap, SetOptions.merge());
                                    mDatabase.collection("users_list").document(user_id)
                                            .collection("pending").document(date).set(userMap, SetOptions.merge());
                                    mDatabase.collection("futsal_list").document(futsal_id)
                                            .collection("Notification").add(notificationMap);
                                })
                                .setNegativeButton("NO", (dialog, which) -> {
                                    //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                                })
                                .show();
                    }
                }

            }
        });

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
        Button pastTimeLayout;
        Button bookBtn;
        boolean own_pending = false;
        ConstraintLayout layout;



        BookTimeViewHolder(View itemView) {
            super(itemView);
            book_time =  itemView.findViewById(R.id.book_time);
            bookBtn =  itemView.findViewById(R.id.book_time_btn);
            layout = itemView.findViewById(R.id.book_time_layout);
            pastTimeLayout = itemView.findViewById(R.id.transparent_layer);

        }

        public void cancelbooking(){
            bookBtn.setTextColor(Color.parseColor("#5FBA3A"));
            bookBtn.setBackgroundResource(R.drawable.green_strok_button);
            bookBtn.setText("Book Now");
            bookBtn.setClickable(true);
            own_pending = false;

        }
        public void firstLoadPendingData(String bookdate,boolean pastTime){
            firstLoadBookedData(bookdate,pastTime);
            if(!pastTime){
                pastTimeLayout.setVisibility(View.VISIBLE);
            }
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
                                    Log.d("CLICKABLE", "Book: " + user_id);
                                    if (mauth.getCurrentUser() != null) {
                                        String uid = mauth.getCurrentUser().getUid();
                                        if (user_id.equals(uid)) {
                                            Log.d("CLICKABLE", "onComplete: " + uid);
                                            bookBtn.setClickable(true);
                                            bookBtn.setBackgroundResource(R.drawable.own_pending_btn);
                                            own_pending = true;
                                            if(!pastTime){
                                                pastTimeLayout.setVisibility(View.VISIBLE);
                                            }

                                        }
                                        firstLoadBookedData(bookdate,pastTime);
                                    }
                                }else {
//                                    bookBtn.setTextColor(Color.parseColor("#5FBA3A"));
//                                    bookBtn.setBackgroundResource(R.drawable.input_field);
//                                    bookBtn.setText("Book Now");
//                                    bookBtn.setClickable(true);

                                }
                                if(!pastTime){
                                    pastTimeLayout.setVisibility(View.VISIBLE);
                                }


                            }
                        }
                    }
                }
            });

        }

        public void firstLoadBookedData(String book_time,boolean pastTime) {
            if(!pastTime){
                pastTimeLayout.setVisibility(View.VISIBLE);
            }
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
                                    bookBtn.setClickable(false);
                                    if(!pastTime){
                                        pastTimeLayout.setVisibility(View.VISIBLE);
                                    }
                                    if (mauth.getCurrentUser() != null) {
                                        String uid = mauth.getCurrentUser().getUid();
                                        Log.d("MAP2", "onComplete: " + uid + "      " + user_id);
                                        if (user_id.equals(uid)) {
                                            bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                            bookBtn.setBackgroundResource(R.drawable.your_booking_button);
                                            bookBtn.setText("Booked");
                                            bookBtn.setClickable(false);
                                            if(!pastTime){
                                                pastTimeLayout.setVisibility(View.VISIBLE);
                                            }
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

