package com.example.futsalnepal;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class FutsalProfile extends Fragment {




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

        return view ;
    }

}
