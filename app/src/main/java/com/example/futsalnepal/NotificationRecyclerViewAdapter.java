package com.example.futsalnepal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.Model.Notifications;

import java.util.List;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private Context context;
    private List<Futsal> futsalList;
    private List<Notifications> notificationsList;


    public NotificationRecyclerViewAdapter(List<Futsal> futsalList, List<Notifications> notificationsList, Context context ){
        this.context = context;
        this.futsalList = futsalList;
        this.notificationsList = notificationsList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_request_user_single_layout, viewGroup, false);
        NotificationViewHolder holder = new NotificationViewHolder(v);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class NotificationViewHolder extends RecyclerView.ViewHolder{

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}