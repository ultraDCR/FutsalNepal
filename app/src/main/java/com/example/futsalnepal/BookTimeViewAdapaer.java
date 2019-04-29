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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookTimeViewAdapaer extends RecyclerView.Adapter<com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder> {

    List<String> list ;
    Context context;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    Activity activity;
    String date;
    String futsal_id;

    public BookTimeViewAdapaer(List<String> list,String date,String futsal_id, Context context,Activity activity) {
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
    public com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context=parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_time_single_layout, parent, false);
        com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder holder = new com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder holder, int position) {
        mauth = FirebaseAuth.getInstance();
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.book_time.setText(list.get(position));
//        holder.bookBtn.setBackgroundResource(R.drawable.input_field);
//        holder.bookBtn.setText("Booking");
//        holder.bookBtn.setTextColor(Color.parseColor("#000000"));
        Log.d("ARRAY4", "onBindViewHolder: "+futsal_id+"  "+date);



        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mauth.getCurrentUser() == null){
                    LoginDialog dialog = new LoginDialog(context,activity);
                    dialog.startLoginDialog();
                    Log.d("pressed","alertdialog");
                }else {
                    String user_id = mauth.getCurrentUser().getUid();
                    Map<String, Object> bookFutsalMap = new HashMap<>();
                        Map<String, Object> userBookMap = new HashMap<>();
                            Map<String, Object> timeuBookMap = new HashMap<>();
                            timeuBookMap.put(list.get(position), FieldValue.serverTimestamp());
                        userBookMap.put(user_id,timeuBookMap);
                    bookFutsalMap.put(date, userBookMap);

                    Map<String, Object> bookUserMap = new HashMap<>();
                        Map<String, Object> futsalBookMap = new HashMap<>();
                            Map<String, Object> timefBookMap = new HashMap<>();
                            timefBookMap.put(list.get(position), FieldValue.serverTimestamp());
                        futsalBookMap.put(futsal_id,timefBookMap);
                    bookUserMap.put(date, futsalBookMap);

                    mDatabase.collection("futsal_list").document(futsal_id).collection("book_info")
                            .document("newrequest").set(bookFutsalMap, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("SUCCESS", "onSuccess: "+holder);
                            holder.bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("FAILER", "onFailure: "+e);
                        }
                    });
                    mDatabase.collection("user_list").document(user_id).collection("book_info").document("pending").set(bookUserMap, SetOptions.merge());

                }
            }
        });

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
    public void insert(int position, String data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(String data) {
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
    }

}

