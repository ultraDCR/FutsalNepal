package com.example.futsalnepal.futsal;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futsalnepal.FutsalIndivisualDetails;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalInfoFragment extends Fragment {

    private TextView fclose, fopen, wPriceM,wPriceD, wPriceE, wePriceM,wePriceD, wePriceE;
    private FirebaseFirestore fDatabase;

    public FutsalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_info, container, false);
        FutsalIndivisualDetails activity = (FutsalIndivisualDetails) getActivity();
        String futsal_id = activity.getMyData();

        fDatabase = FirebaseFirestore.getInstance();

         fclose = view.findViewById(R.id.closing_time);
         fopen = view.findViewById(R.id.opening_time);
         wPriceM = view.findViewById(R.id.fwd_morning_price);
         wPriceD = view.findViewById(R.id.fwd_day_price);
         wPriceE = view.findViewById(R.id.fwd_evening_price);
         wePriceM = view.findViewById(R.id.fwe_morning_price);
         wePriceD = view.findViewById(R.id.fwe_day_price);
         wePriceE = view.findViewById(R.id.fwe_evening_price);

        fDatabase.collection("futsal_list").document(futsal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                            String open_time = task.getResult().getString("opening_hour");
                            String close_time = task.getResult().getString("closing_hour");
                            Map<String, String> week_price = (Map<String, String>) task.getResult().get("week_end_price");
                            String w_morning = week_price.get("morning_price");
                            String w_day = week_price.get("day_price");
                            String w_evening = week_price.get("evening_price");
                            String we_morning = week_price.get("morning_price");
                            String we_day = week_price.get("day_price");
                            String we_evening = week_price.get("evening_price");


                            fopen.setText(open_time);
                            fclose.setText(close_time);
                            wPriceM.setText(w_morning);
                            wPriceD.setText(w_day);
                            wPriceE.setText(w_evening);
                            wePriceM.setText(we_morning);
                            wePriceD.setText(we_day);
                            wePriceE.setText(we_evening);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });


        return view;
    }

}
