package com.example.futsalnepal.users;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.Model.BookingFutsal;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PendingRequestRecyclerView extends RecyclerView.Adapter<PendingRequestRecyclerView.FutsalViewHolder>  {
    List<BookingFutsal> list;
    Context context;
    String date;
    FirebaseAuth mauth;
    FirebaseFirestore mDatabase;
    String futsal_id;
    String bookTime[] = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM","10PM", "11PM"};

    public PendingRequestRecyclerView(String date, List<BookingFutsal> list, Context context) {
        this.list = list;
        this.context = context;
        this.date = date;
        mauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
    }

    @Override
    public PendingRequestRecyclerView.FutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_request_layout, parent, false);
        PendingRequestRecyclerView.FutsalViewHolder holder = new PendingRequestRecyclerView.FutsalViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(PendingRequestRecyclerView.FutsalViewHolder holder, int position) {

        holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.cv.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));


        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.name.setText(list.get(position).futsal_name);
        Map<String,Object> futsal_address = list.get(position).location;
        String location =futsal_address.get("vdc").toString()+", "+futsal_address.get("district").toString();
        holder.address.setText(location);
        String from_time = list.get(position).time;

        //setting from and to time in time
        int i = Arrays.asList(bookTime).indexOf(from_time);
        String to_time  = bookTime[i+1];
        Log.e("APPTEST4", "testing dates  "+from_time+"  "+i+"  "+to_time);
        holder.time.setText(from_time+" - "+to_time);


        // logic for testing date has past or not
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy hha", Locale.US);
            String date3 = date+" "+list.get(position).time;
            Log.d("ERROR", "nothing parse"+date3);
            Date date1 = sdf.parse(date3);
            Log.d("ERROR", "date3 parsed: "+date1);

            Date date2 = sdf.parse(sdf.format(new Date()));
            Log.e("APPTEST1", "testing dates"+date3+"-"+date1+"-"+date2);
//        if(date1.before(date2)){
//            Log.e("app", "Date1 is before Date2");
//            return true ;
//        }
            if(date1.equals(date2) || date1.before(date2)){
                Log.e("APPTEST", "Date1 is after Date2");
                holder.layout.setBackgroundResource(R.drawable.booked_history_bg);
                holder.cancleBtn.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR", "onBindViewHolder: "+e);

        }

        RequestOptions placeholderRequest = new RequestOptions();
        Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(list.get(position).futsal_logo).into(holder.profile);

        holder.ratingBar.setRating(list.get(position).overall_rating);
        holder.cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = mauth.getCurrentUser().getUid();
                String futsal_id= list.get(position).futsal_id;

                Map<String, Object> userMap = new HashMap<>();
                Map<String, Object> timeMap = new HashMap<>();
                timeMap.put(list.get(position).time, FieldValue.delete());
                userMap.put(futsal_id, timeMap);


                Map<String, Object> futsalMap = new HashMap<>();
                Map<String, Object> timeMap1 = new HashMap<>();
                timeMap1.put(list.get(position).time, FieldValue.delete());
                futsalMap.put(user_id, timeMap1);

                String message = "Booking request for "+date+" at "+list.get(position).time+"was cancled";
                Map<String, Object> notificationMap = new HashMap<>();
                notificationMap.put("from", user_id);
                notificationMap.put("type", "removed");
                notificationMap.put("message", message);
                notificationMap.put("status","notseen");
                notificationMap.put("timestamp",FieldValue.serverTimestamp());

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to cancle booking request of "+date +" "+list.get(position).time +" ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                mDatabase.collection("users_list").document(user_id)
                                        .collection("pending").document(date).set(userMap, SetOptions.merge());
                                mDatabase.collection("futsal_list").document(futsal_id)
                                        .collection("Notification").add(notificationMap);
                                mDatabase.collection("futsal_list").document(futsal_id)
                                        .collection("newrequest").document(date).set(futsalMap, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mDatabase.collection("users_list").document(user_id)
                                                        .collection("pending").document(date).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()){
                                                            if(task.getResult().exists()){
                                                                Map<String,Object> useMap = (Map<String, Object>) task.getResult().get(futsal_id);
                                                                if(useMap.size() < 1){
                                                                    mDatabase.collection("users_list").document(user_id)
                                                                            .collection("pending").document(date).delete();
                                                                    mDatabase.collection("futsal_list").document(futsal_id)
                                                                            .collection("newrequest").document(date).delete();
                                                                }else{
                                                                    mDatabase.collection("users_list").document(user_id)
                                                                            .collection("pending").document(date).set(userMap, SetOptions.merge());
                                                                    mDatabase.collection("futsal_list").document(futsal_id)
                                                                            .collection("newrequest").document(date).set(futsalMap, SetOptions.merge());
                                                                }
                                                                notifyDataSetChanged();
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
        //animate(holder);


    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        Log.d("DATETEST8", "getItemCount: "+list);
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, BookingFutsal data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(BookingFutsal data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

//    @Override
//    public Filter getFilter() {
//        return dateFilter;
//    }
//
//    private Filter dateFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Futsal> filterList = new ArrayList<>();
//            if(constraint == null || constraint.length() == 0){
//                filterList.addAll(filter);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for(Futsal item : filter){
//                    if(   )
//                }
//            }
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    };

    public class FutsalViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView time;
        ImageView profile;
        RatingBar ratingBar;
        ConstraintLayout layout;
        Button cancleBtn;
        CardView cv;

        FutsalViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.p_futsal_name);
            address = itemView.findViewById(R.id.p_futsal_address);
            time = itemView.findViewById(R.id.p_futsal_available_time);
            profile = itemView.findViewById(R.id.p_futsal_profile);
            ratingBar = itemView.findViewById(R.id.p_futsal_rating);
            layout = itemView.findViewById(R.id.background_pending);
            cancleBtn =itemView.findViewById(R.id.cancle_btn);
            cv = itemView.findViewById(R.id.futsal_card_view);

        }
    }


}