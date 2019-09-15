package com.example.futsalnepal.users;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.futsalnepal.futsal.FutsalBookedRecyclerView;
import com.example.futsalnepal.futsal.FutsalHistoryRecyclerView;
import com.example.futsalnepal.futsal.FutsalNewRequestRecyclerView;
import com.example.futsalnepal.Model.SectionModel;
import com.example.futsalnepal.R;

import java.util.List;

public class DateSectionUserRecyclerViewAdapter extends RecyclerView.Adapter<DateSectionUserRecyclerViewAdapter.SectionViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = (TextView) itemView.findViewById(R.id.date_section_text);
            Log.d("DATETEST11", "SectionViewHolder: "+sectionLabel.getText());
            itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private Context context;
    private List<SectionModel> sectionModelArrayList;
    String recyclertype;

    public DateSectionUserRecyclerViewAdapter(String recyclertype, Context context, List<SectionModel> sectionModelArrayList) {
        this.context = context;
        this.sectionModelArrayList = sectionModelArrayList;
        this.recyclertype= recyclertype;
        Log.d("DATETEST12", "SectionViewHolder: "+sectionModelArrayList);

    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_section_custom_row, parent, false);
        return new DateSectionUserRecyclerViewAdapter.SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        Log.d("DATETEST10",""+holder+""+position);

        holder.sectionLabel.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));


        final SectionModel sectionModel = sectionModelArrayList.get(position);
        holder.sectionLabel.setText(sectionModel.getSectionLabel());

        //recycler view for items
         //holder.itemRecyclerView.setHasFixedSize(true);
        //holder.itemRecyclerView.setNestedScrollingEnabled(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        Log.d("DATETEST10",""+sectionModel.getUserArray());
        if(recyclertype.equals("request")) {
            FutsalNewRequestRecyclerView adapter = new FutsalNewRequestRecyclerView(sectionModel.getSectionLabel(),sectionModel.getUserArray(), context);
            holder.itemRecyclerView.setAdapter(adapter);
        }else if(recyclertype.equals("booked")){
            FutsalBookedRecyclerView adapter = new FutsalBookedRecyclerView(sectionModel.getSectionLabel(),sectionModel.getUserArray(), context);
            holder.itemRecyclerView.setAdapter(adapter);
        }else{
            FutsalHistoryRecyclerView adapter = new FutsalHistoryRecyclerView(sectionModel.getSectionLabel(),sectionModel.getUserArray(), context);
            holder.itemRecyclerView.setAdapter(adapter);
        }


    }

    @Override
    public int getItemCount() {
        return sectionModelArrayList.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
