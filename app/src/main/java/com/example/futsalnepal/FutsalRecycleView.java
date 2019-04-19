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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Collections;
import java.util.List;

public class FutsalRecycleView extends RecyclerView.Adapter<FutsalRecycleView.FutsalViewHolder> {

        List<Futsal> list ;
        Context context;

        public FutsalRecycleView(List<Futsal> list, Context context) {
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

            final String futsalId = list.get(position).FutsalId;

            holder.setFutsalName(list.get(position).getFutsal_name());
            holder.setFutsalAddress(list.get(position).getFutsal_address());
            holder.setFutsalTime(list.get(position).getOpening_hour(),list.get(position).getClosing_hour());
            holder.setFutsalLogo(list.get(position).getFutsal_logo());
           // holder.setFutsalRating(list.get(position).getRating());

            //animate(holder);


            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent futsal = new Intent(context, FutsalIndivisualDetails.class);
                    futsal.putExtra("futsal_id", futsalId);
                    context.startActivity(futsal);
                }
            });

        }

        @Override
        public int getItemCount() {
            //returns the number of elements the RecyclerView will display
            if(list != null) {
                return list.size();
            } else {
                return 0;
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, Futsal data) {
            list.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(Futsal data) {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemRemoved(position);
        }

    public class FutsalViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView name;
        private TextView address;
        private TextView time;
        private ImageView profile;
        private RatingBar ratingBar;
        private View mView;

        public FutsalViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            cv =  itemView.findViewById(R.id.futsal_card_view);
            profile =  itemView.findViewById(R.id.futsal_profile);
            ratingBar = itemView.findViewById(R.id.futsal_rating);

        }

        public void setFutsalName(String futsal_name){
            name =  itemView.findViewById(R.id.futsal_name);
            name.setText(futsal_name);
        }

        public void setFutsalAddress(String futsal_address){
            address =  itemView.findViewById(R.id.futsal_address);
            address.setText(futsal_address);
        }
        public void setFutsalTime(String open, String close){
            time =  itemView.findViewById(R.id.futsal_available_time);
            time.setText(open+"-"+close);
        }
        public void setFutsalLogo(String futsal_logo){
            profile =  itemView.findViewById(R.id.futsal_profile);
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.logo);

            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(profile);
        }
//        public void setFutsalRating(float rating){
//            ratingBar =  itemView.findViewById(R.id.futsal_rating);
//            ratingBar.setRating(rating);
//        }

    }

}

