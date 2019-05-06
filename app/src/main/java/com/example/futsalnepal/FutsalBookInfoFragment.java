package com.example.futsalnepal;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FutsalBookInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_futsal_book_info, container, false);

//        Toolbar toolbar = view.findViewById(R.id.book_information_toolbar);
//        view.setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
