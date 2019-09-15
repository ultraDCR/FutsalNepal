package com.example.futsalnepal.futsal;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.futsalnepal.R;

public class FutsalBookInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_book_info, container, false);

        Toolbar toolbar = view.findViewById(R.id.book_information_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

        //View Pager
        ViewPager viewPager =  view.findViewById(R.id.book_information_vpager);
        FutsalBookInformationPagerAdapter fPagerAdapter = new FutsalBookInformationPagerAdapter(this.getActivity(), getChildFragmentManager());
        viewPager.setAdapter(fPagerAdapter);
        TabLayout tablayout =  view.findViewById(R.id.book_information_tablayout);
        tablayout.setupWithViewPager(viewPager);

        return view;
    }

    // for toolbar
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }
}
