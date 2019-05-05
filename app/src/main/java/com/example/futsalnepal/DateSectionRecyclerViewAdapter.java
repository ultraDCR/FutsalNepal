package com.example.futsalnepal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futsalnepal.Model.SectionModel;

import java.util.List;

public class DateSectionRecyclerViewAdapter extends RecyclerView.Adapter<DateSectionRecyclerViewAdapter.SectionViewHolder> {


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

    public DateSectionRecyclerViewAdapter(String recyclertype,Context context, List<SectionModel> sectionModelArrayList) {
        this.context = context;
        this.sectionModelArrayList = sectionModelArrayList;
        this.recyclertype= recyclertype;
        Log.d("DATETEST12", "SectionViewHolder: "+sectionModelArrayList);

    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_section_custom_row, parent, false);
        return new DateSectionRecyclerViewAdapter.SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        Log.d("DATETEST10",""+holder+""+position);

        final SectionModel sectionModel = sectionModelArrayList.get(position);
        holder.sectionLabel.setText(sectionModel.getSectionLabel());

        //recycler view for items
        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
        Log.d("DATETEST10",""+sectionModel.getFutsals());
        if(recyclertype.equals("pending")) {
            PendingRequestRecyclerView adapter = new PendingRequestRecyclerView(sectionModel.getFutsals(), context);
            holder.itemRecyclerView.setAdapter(adapter);
        }else if(recyclertype.equals("booked")){
            BookedRecyclerView adapter = new BookedRecyclerView(sectionModel.getFutsals(), context);
            holder.itemRecyclerView.setAdapter(adapter);
        }else{
            HistoryRecyclerView adapter = new HistoryRecyclerView(sectionModel.getFutsals(), context);
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