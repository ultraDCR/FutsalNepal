package com.example.futsalnepal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.Futsal;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class FutsalRecycleView extends RecyclerView.Adapter<FutsalRecycleView.FutsalViewHolder> {

        List<Futsal> list ;
        Context context;
        Activity activity;

        public FutsalRecycleView(List<Futsal> list, Context context, Activity activity) {
            this.list = list;
            this.context = context;
            this.activity = activity;

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

//            holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));
//
//            holder.cv.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));

            holder.setFutsalName(list.get(position).getFutsal_name());
            holder.setFutsalAddress(list.get(position).getLocation());
            holder.setFutsalTime(list.get(position).getOpening_hour(),list.get(position).getClosing_hour());
            holder.setFutsalLogo(list.get(position).getFutsal_logo());
            holder.setFutsalRating(list.get(position).getOverall_rating());
            if(list.get(position).getDistance() != 0){
                holder.setDistance(list.get(position).getDistance());
            }
           // holder.setFutsalRating(list.get(position).getRating());

            //animate(holder);


            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent futsal = new Intent(context, FutsalIndivisualDetails.class);
                    futsal.putExtra("futsal_id", futsalId);
                    // start the new activity
                    context.startActivity(futsal);
                    activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            });
            holder.bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent futsal = new Intent(context, FutsalIndivisualDetails.class);
                    futsal.putExtra("futsal_id", futsalId);
                    // start the new activity
                    context.startActivity(futsal);
                    activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
        private TextView time,distancetxt;
        private ImageView profile;
        private RatingBar ratingBar;
        private Button bookNow;
        private View mView;
        private ConstraintLayout container;

        public FutsalViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            bookNow = mView.findViewById(R.id.book_now_btn);

            cv =  itemView.findViewById(R.id.futsal_card_view);
            profile =  itemView.findViewById(R.id.futsal_profile);
            ratingBar = itemView.findViewById(R.id.futsal_rating);
            container = itemView.findViewById(R.id.content_layout);

        }

        public void setFutsalName(String futsal_name){
            name =  mView.findViewById(R.id.futsal_name);
            name.setText(futsal_name);
        }

        public void setFutsalAddress(Map<String,Object> futsal_address){
            address =  mView.findViewById(R.id.location_search);
            String location =futsal_address.get("vdc").toString()+", "+futsal_address.get("district").toString();
            address.setText(location);
        }
        public void setFutsalTime(String open, String close){
            time =  mView.findViewById(R.id.futsal_available_time);
            time.setText(open+" - "+close);
        }
        public void setFutsalLogo(String futsal_logo){
            profile =  mView.findViewById(R.id.futsal_profile);
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.futsal_time_logo);

            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(profile);
        }
        public void setFutsalRating(float rating){
            ratingBar =  mView.findViewById(R.id.futsal_rating);
            ratingBar.setRating(rating);
        }

        public void setDistance(double distance) {
            distancetxt =  mView.findViewById(R.id.distance);
            String d;
            if(distance <1000) {
                d = (new DecimalFormat("#").format(distance))+"M far";
            }else{
                d = (new DecimalFormat("#.#").format(distance/1000))+"KM far";
            }
            bookNow.setVisibility(View.GONE);
            distancetxt.setVisibility(View.VISIBLE);
            distancetxt.setText(d);
        }
    }

}

