package com.example.futsalnepal;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingFutsal;
import com.example.futsalnepal.Model.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder> {
    private Context context;
    //private List<Futsal> futsalList;
    private List<Notifications> notificationsList;
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mAuth;
    private String user_id, user_type;


    public NotificationRecyclerViewAdapter(String user_type, List<Notifications> notificationsList, Context context) {
        this.context = context;
//        this.futsalList = futsalList;
        this.user_type = user_type;
        this.notificationsList = notificationsList;
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_single_layout, viewGroup, false);
        NotificationViewHolder holder = new NotificationViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int i) {
        user_id = mAuth.getCurrentUser().getUid();

        holder.futsalImage.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));



        holder.setSenderInfo(user_type,notificationsList.get(i).from);
        holder.fmessage.setText(notificationsList.get(i).message);
        holder.setTime(notificationsList.get(i).timestamp);

        String id = notificationsList.get(i).NotificationId;
        String status = notificationsList.get(i).status;
//        if(status.equals("notseen")){
//            holder.cardView.setBackgroundResource(R.color.light_green);
//        }
//        holder.cardView.setOnClickListener((view)->{
            Map<String,Object> not_map = new HashMap<>();
            not_map.put("status","seen");
            if(status.equals("notseen")) {
                if(user_type.equals("user")) {
                    mDatabase.collection("users_list").document(user_id).collection("Notification").document(id).update(not_map);
                }else{
                    mDatabase.collection("futsal_list").document(user_id).collection("Notification").document(id).update(not_map);
                }
            }
//            Intent notify = new Intent(context, FutsalIndivisualDetails.class);
//            notify.putExtra("futsal_id",notificationsList.get(i).from);
//            context.startActivity(notify);
//        });

    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Notifications data) {
        notificationsList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookingFutsal data) {
        int position = notificationsList.indexOf(data);
        notificationsList.remove(position);
        notifyItemRemoved(position);
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        private TextView futsalName,fmessage,time;
        private ImageView futsalImage;
        private CardView cardView;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            futsalName = itemView.findViewById(R.id.futsal_name);
            futsalImage = itemView.findViewById(R.id.futsal_image);
            fmessage = itemView.findViewById(R.id.message);
            cardView = itemView.findViewById(R.id.notification_cv);
            time = itemView.findViewById(R.id.send_time);

        }
        public void setSenderInfo(String type, String fromId){
            if(type.equals("user")) {
                mDatabase.collection("futsal_list").document(fromId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().get("futsal_name").toString();
                            String image = task.getResult().get("futsal_logo").toString();
                            futsalName.setText(name);

                            RequestOptions placeholderRequest = new RequestOptions();
                            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(image).into(futsalImage);

                        }

                    }
                });
            }
            else{
                mDatabase.collection("users_list").document(fromId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String name = task.getResult().get("user_full_name").toString();
                            String image = task.getResult().get("user_profile_image").toString();
                            futsalName.setText(name);

                            RequestOptions placeholderRequest = new RequestOptions();
                            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(image).into(futsalImage);

                        }

                    }
                });
            }

        }

        public void setTime(Date timestamp){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mma", Locale.US);
            String sendTime = sdf.format(timestamp);
            time.setText(sendTime);
        }

    }
}