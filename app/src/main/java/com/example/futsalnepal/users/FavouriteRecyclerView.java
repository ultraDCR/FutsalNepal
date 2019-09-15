package com.example.futsalnepal.users;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.FutsalIndivisualDetails;
import com.example.futsalnepal.Model.Futsal;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavouriteRecyclerView extends RecyclerView.Adapter<FavouriteRecyclerView.FavFutsalViewHolder> {
        FirebaseFirestore db;
        FirebaseAuth mAuth;
        List<Futsal> list ;
        Context context;

    public FavouriteRecyclerView(List<Futsal> list, Context context) {
            this.list = list;
            this.context = context;
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            }

    @Override
    public FavFutsalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //Inflate the layout, initialize the View Holder
            context=parent.getContext();
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.futsal_list_item, parent, false);
            FavFutsalViewHolder holder = new FavFutsalViewHolder(v);
            return holder;

            }

    @Override
    public void onBindViewHolder(FavFutsalViewHolder holder, int position) {

    final String futsalId = list.get(position).FutsalId;

        holder.profile.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));

        holder.cv.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_transition));


        holder.setFutsalName(list.get(position).getFutsal_name());
            holder.setFutsalAddress(list.get(position).getFutsal_address());
            holder.setFutsalTime(list.get(position).getOpening_hour(),list.get(position).getClosing_hour());
            holder.setFutsalLogo(list.get(position).getFutsal_logo());
            holder.setFutsalRating(list.get(position).getOverall_rating());
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


    public void deleteItem(int position){
        String user_id = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users_list").document(user_id);
        docRef.update("favourite_futsal", FieldValue.arrayRemove(list.get(position).FutsalId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                notifyDataSetChanged();
                Toast.makeText(context, "delete_from_favoutote", Toast.LENGTH_SHORT).show();
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

    public class FavFutsalViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView name;
        private TextView address;
        private TextView time;
        private ImageView profile;
        private RatingBar ratingBar;
        private View mView;

        public FavFutsalViewHolder(View itemView) {
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
            address =  itemView.findViewById(R.id.location_search);
            address.setText(futsal_address);
        }
        public void setFutsalTime(String open, String close){
            time =  itemView.findViewById(R.id.futsal_available_time);
            time.setText(open+"-"+close);
        }
        public void setFutsalLogo(String futsal_logo){
            profile =  itemView.findViewById(R.id.futsal_profile);
            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.futsal_time_logo);

            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(profile);
        }
        public void setFutsalRating(float rating){
            ratingBar =  itemView.findViewById(R.id.futsal_rating);
            ratingBar.setRating(rating);
        }

    }

}
