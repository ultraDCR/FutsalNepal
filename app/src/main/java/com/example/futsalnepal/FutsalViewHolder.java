package com.example.futsalnepal;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FutsalViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView name;
    TextView address;
    TextView time;
    ImageView profile,add,clock,rating1,rating2,rating3,rating4,rating5;

    FutsalViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.futsal_card_view);
        name = (TextView) itemView.findViewById(R.id.futsal_name);
        address = (TextView) itemView.findViewById(R.id.futsal_address);
        time = (TextView) itemView.findViewById(R.id.futsal_available_time);
        profile = (ImageView) itemView.findViewById(R.id.futsal_profile);
        add = itemView.findViewById(R.id.location_icon);
        clock = itemView.findViewById(R.id.time_icon);
        rating1 = itemView.findViewById(R.id.futsal_rating_1);
        rating2 = itemView.findViewById(R.id.futsal_rating_2);
        rating3 = itemView.findViewById(R.id.futsal_rating_3);
        rating4 = itemView.findViewById(R.id.futsal_rating_4);
        rating5 = itemView.findViewById(R.id.futsal_rating_5);

    }
}