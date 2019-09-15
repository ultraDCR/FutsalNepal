package com.example.futsalnepal.futsal;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalProfile extends Fragment {


    private TextView fPhone, fAddress,fName, fclose, fopen, wPriceM,wPriceD, wPriceE, wePriceM,wePriceD, wePriceE;
    private CircleImageView fLogo;
    private Button editBtn;
    private String futsal_id;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    public FutsalProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_profile, container, false);
        ConstraintLayout placeHolder =  view.findViewById(R.id.include_futsal_info);
        getLayoutInflater().inflate(R.layout.fragment_futsal_info, placeHolder);

        Toolbar toolbar= view.findViewById(R.id.profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        fPhone = view.findViewById(R.id.f_phone);
        fAddress = view.findViewById(R.id.f_address);
        fLogo = view.findViewById(R.id.f_logo);
        fName  = view.findViewById(R.id.f_name);
        editBtn = view.findViewById(R.id.edit_btn);

        fclose = view.findViewById(R.id.closing_time);
        fopen = view.findViewById(R.id.opening_time);
        wPriceM = view.findViewById(R.id.fwd_morning_price);
        wPriceD = view.findViewById(R.id.fwd_day_price);
        wPriceE = view.findViewById(R.id.fwd_evening_price);
        wePriceM = view.findViewById(R.id.fwe_morning_price);
        wePriceD = view.findViewById(R.id.fwe_day_price);
        wePriceE = view.findViewById(R.id.fwe_evening_price);
        editBtn.setOnClickListener(v ->{
            Intent settingIntent = new Intent(getActivity(), FutsalInfoEdit.class);
            startActivity(settingIntent);
        });

        if(mAuth.getCurrentUser() != null) {
            futsal_id = mAuth.getCurrentUser().getUid();
            mDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if(task.getResult().exists()) {
                            String futsal_name = task.getResult().getString("futsal_name");
                            String futsal_address = task.getResult().getString("futsal_address");
                            String futsal_phone = task.getResult().getString("futsal_phone");
                            String futsal_logo = task.getResult().getString("futsal_logo");
                            String open_time = task.getResult().getString("opening_hour");
                            String close_time = task.getResult().getString("closing_hour");
                            Map<String, String> week_price = (Map<String, String>) task.getResult().get("week_end_price");
                            String w_morning = week_price.get("morning_price");
                            String w_day = week_price.get("day_price");
                            String w_evening = week_price.get("evening_price");
                            String we_morning = week_price.get("morning_price");
                            String we_day = week_price.get("day_price");
                            String we_evening = week_price.get("evening_price");

                            fPhone.setText(futsal_phone);
                            fName.setText(futsal_name);
                            fAddress.setText(futsal_address);

                            fopen.setText(open_time);
                            fclose.setText(close_time);
                            wPriceM.setText(w_morning);
                            wPriceD.setText(w_day);
                            wPriceE.setText(w_evening);
                            wePriceM.setText(we_morning);
                            wePriceD.setText(we_day);
                            wePriceE.setText(we_evening);

                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.logo_placeholder_circle);
                            if(getContext() != null) {
                                Glide.with(getContext()).setDefaultRequestOptions(placeholderRequest).load(futsal_logo).into(fLogo);
                            }
                        }
                    }
                }
            });
        }
        return view ;
    }

}
