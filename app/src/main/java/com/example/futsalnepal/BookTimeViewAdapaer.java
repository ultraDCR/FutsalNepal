package com.example.futsalnepal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.futsalnepal.Model.BookTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BookTimeViewAdapaer extends RecyclerView.Adapter<com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder> {

    List<BookTime> list ;
    Context context;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    Activity activity;
    String date;
    String futsal_id;

    public BookTimeViewAdapaer(List<BookTime> list,String date,String futsal_id, Context context,Activity activity) {
        this.list = list;
        this.date = date;
        this.futsal_id = futsal_id;
        Log.d("FUTSAL", "BookTimeViewAdapaer: "+futsal_id);
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
        mauth = FirebaseAuth.getInstance();
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.book_time.setText(list.get(position).book_time);
        Log.d("ARRAY4", "onBindViewHolder: "+futsal_id+"  "+date);
        boolean pastTime = holder.pastTimeDisable(list.get(position).book_time, date);
        if(pastTime) {
            holder.firstLoadPendingData(list.get(position).book_time);
            holder.firstLoadBookedData(list.get(position).book_time);


            holder.bookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mauth.getCurrentUser() == null) {
                        LoginDialog dialog = new LoginDialog(context, activity);
                        dialog.startLoginDialog();
                        Log.d("pressed", "alertdialog");
                    } else {
                        String user_id = mauth.getCurrentUser().getUid();
                        Map<String, Object> bookFutsalMap = new HashMap<>();
                        Map<String, Object> userBookMap = new HashMap<>();
                        Map<String, Object> timeuBookMap = new HashMap<>();
                        timeuBookMap.put(list.get(position).book_time, FieldValue.serverTimestamp());
                        userBookMap.put(user_id, timeuBookMap);
                        bookFutsalMap.put(date, userBookMap);

                        Map<String, Object> bookUserMap = new HashMap<>();
                        Map<String, Object> futsalBookMap = new HashMap<>();
                        Map<String, Object> timefBookMap = new HashMap<>();
                        timefBookMap.put(list.get(position).book_time, FieldValue.serverTimestamp());
                        futsalBookMap.put(futsal_id, timefBookMap);
                        bookUserMap.put(date, futsalBookMap);

                        mDatabase.collection("futsal_list").document(futsal_id).collection("book_info")
                                .document("newrequest").set(bookFutsalMap, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("SUCCESS", "onSuccess: " + holder);
                                        holder.setPending();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("FAILER", "onFailure: " + e);
                            }
                        });
                        mDatabase.collection("user_list").document(user_id).collection("book_info").document("pending").set(bookUserMap, SetOptions.merge());

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



        BookTimeViewHolder(View itemView) {
            super(itemView);
            book_time =  itemView.findViewById(R.id.book_time);
            bookBtn =  itemView.findViewById(R.id.book_time_btn);


        }

        public void setPending() {
            bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
            bookBtn.setBackgroundResource(R.drawable.pending_button);
            bookBtn.setText("Pending");
            bookBtn.setClickable(false);
        }
        public void firstLoadPendingData(String bookdate){
            mDatabase.collection("futsal_list").document(futsal_id)
                    .collection("book_info").document("newrequest").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            if(task.getResult().get(date) != null){
                                Map<String, Object> dateRequested = (Map<String, Object>) task.getResult().get(date);
//                                if(mauth.getCurrentUser() != null) {
//                                    String uid = mauth.getCurrentUser().getUid();
                                    for (String user_id: dateRequested.keySet() ) {
                                        if (dateRequested.get(user_id) != null) {
                                            Map<String, Object> userId = (Map<String, Object>) dateRequested.get(user_id);
                                            if (userId.get(bookdate) != null) {
                                                bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                                bookBtn.setBackgroundResource(R.drawable.pending_button);
                                                bookBtn.setText("Pending");
                                                bookBtn.setClickable(false);
                                            }
                                        }
                                    }
                                //}
                            }
                        }

                    }
                }
            });

        }

        public void firstLoadBookedData(String book_time) {
            mDatabase.collection("futsal_list").document(futsal_id)
                    .collection("book_info").document("booked").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().exists()){
                                    Log.d("MAP_TEST", "onComplete: "+task.getResult());
                                    if(task.getResult().get(date) != null){
                                        Map<String, Object> dateRequested = (Map<String, Object>) task.getResult().get(date);
                                            for (String user_id: dateRequested.keySet() ){
                                                if (dateRequested.get(user_id) != null) {
                                                    Log.d("MAP_TEST", "onComplete: " + dateRequested.get(user_id));
                                                    Map<String, Object> userId = (Map<String, Object>) dateRequested.get(user_id);
                                                    if (userId.get(book_time) != null) {
                                                        bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                                        bookBtn.setBackgroundResource(R.drawable.already_booked_button);
                                                        bookBtn.setText("Already Booked");
                                                        bookBtn.setClickable(false);
                                                        String uid = mauth.getCurrentUser().getUid();
                                                        Log.d("MAP2", "onComplete: " + uid + "      " + user_id);
                                                        if (user_id.equals(uid)) {
                                                            bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                                                            bookBtn.setBackgroundResource(R.drawable.your_booking_button);
                                                            bookBtn.setText("Booked");
                                                            bookBtn.setClickable(false);
                                                        }
                                                    }
                                                }
                                            }


                                       // }
                                    }
                                }

                            }
                        }
                    });

        }

        public Boolean pastTimeDisable(String book_time, String date) {
            String Time[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};
            SimpleDateFormat sdf = new SimpleDateFormat("hha");
            String currentTime = sdf.format(new Date());
            String currentDate = DateFormat.getDateInstance().format(new Date());
            int timeIndex = Arrays.asList(Time).indexOf(currentTime);
            int bookIndex = Arrays.asList(Time).indexOf(book_time);

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

