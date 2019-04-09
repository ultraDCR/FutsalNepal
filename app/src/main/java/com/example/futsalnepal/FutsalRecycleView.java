package com.example.futsalnepal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class FutsalRecycleView extends RecyclerView.Adapter<FutsalRecycleView.FutsalViewHolder> {

        List<Data> list = Collections.emptyList();
        Context context;

        public FutsalRecycleView(List<Data> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            context=parent.getContext();
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_list_item, parent, false);
            FutsalViewHolder holder = new FutsalViewHolder(v);
            return holder;

        }

        @Override
        public void onBindViewHolder(FutsalViewHolder holder, int position) {

            //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
            holder.name.setText(list.get(position).name);
            holder.address.setText(list.get(position).address);
            holder.time.setText(list.get(position).time);
            holder.profile.setImageResource(list.get(position).imageId);
            holder.ratingBar.setRating(list.get(position).rating);

            //animate(holder);


            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent futsal = new Intent(context, FutsalIndivisualDetails.class);
                    futsal.putExtra("futsal_name", list.get(position).name);
                    context.startActivity(futsal);
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
        public void insert(int position, Data data) {
            list.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(Data data) {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemRemoved(position);
        }

    public class FutsalViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView address;
        TextView time;
        ImageView profile,add,clock,rating1,rating2,rating3,rating4,rating5;
        RatingBar ratingBar;

        FutsalViewHolder(View itemView) {
            super(itemView);
            cv =  itemView.findViewById(R.id.futsal_card_view);
            name =  itemView.findViewById(R.id.futsal_name);
            address =  itemView.findViewById(R.id.futsal_address);
            time =  itemView.findViewById(R.id.futsal_available_time);
            profile =  itemView.findViewById(R.id.futsal_profile);
            add = itemView.findViewById(R.id.location_icon);
            ratingBar = itemView.findViewById(R.id.futsal_rating);

        }
    }

}

