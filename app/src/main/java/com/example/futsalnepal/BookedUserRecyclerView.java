package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class BookedUserRecyclerView extends RecyclerView.Adapter<com.example.futsalnepal.BookedUserRecyclerView.BookedUserViewHolder>{

    List<BookedUser> list=Collections.emptyList();
    Context context;

    public BookedUserRecyclerView(List<BookedUser> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public com.example.futsalnepal.BookedUserRecyclerView.BookedUserViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        //Inflate the layout, initialize the View Holder
        context=parent.getContext();
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.booked_info_layout_futsaluser,parent,false);
        com.example.futsalnepal.BookedUserRecyclerView.BookedUserViewHolder holder=new com.example.futsalnepal.BookedUserRecyclerView.BookedUserViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(com.example.futsalnepal.BookedUserRecyclerView.BookedUserViewHolder holder,int position){

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).name);
        holder.time.setText(list.get(position).time);
        holder.date.setText(list.get(position).date);
        holder.phone.setText(list.get(position).phone);

        //animate(holder);


    }

    @Override
    public int getItemCount(){
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position,BookedUser book_user){
        list.add(position,book_user);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookedUser book_user){
        int position=list.indexOf(book_user);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class BookedUserViewHolder extends RecyclerView.ViewHolder {

        TextView name,time,date,phone;



        BookedUserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.a_person_name_);
            time = itemView.findViewById(R.id.a_book_date);
            date = itemView.findViewById(R.id.a_book_time);
            phone = itemView.findViewById(R.id.a_book_phone);
        }
    }
}

