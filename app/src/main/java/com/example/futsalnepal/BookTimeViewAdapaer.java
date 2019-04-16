package com.example.futsalnepal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;


public class BookTimeViewAdapaer extends RecyclerView.Adapter<com.example.futsalnepal.BookTimeViewAdapaer.BookTimeViewHolder> {

    List<BookTime> list = Collections.emptyList();
    Context context;
    FirebaseAuth mauth;
    Activity activity;

    public BookTimeViewAdapaer(List<BookTime> list, Context context,Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;
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
        holder.book_time.setText(list.get(position).book_time);


        //animate(holder);


        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mauth.getCurrentUser() == null){
                    LoginDialog dialog = new LoginDialog(context,activity);
                    dialog.startLoginDialog();
                    Log.d("pressed","alertdialog");
                }else {
                    holder.bookBtn.setBackgroundResource(R.drawable.pending_button);
                    holder.bookBtn.setText("Pending...");
                    holder.bookBtn.setTextColor(Color.parseColor("#FFFFFF"));
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
    }

}

