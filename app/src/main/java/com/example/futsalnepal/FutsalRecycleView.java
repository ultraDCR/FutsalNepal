package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class FutsalRecycleView extends RecyclerView.Adapter<FutsalViewHolder> {

        List<Data> list = Collections.emptyList();
        Context context;

        public FutsalRecycleView(List<Data> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
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

            //animate(holder);

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

}
