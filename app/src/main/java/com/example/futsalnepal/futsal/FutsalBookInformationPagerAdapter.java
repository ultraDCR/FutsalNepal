package com.example.futsalnepal.futsal;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.futsalnepal.R;

public class FutsalBookInformationPagerAdapter extends FragmentPagerAdapter{

    private Context fContext;

    public FutsalBookInformationPagerAdapter(Context acontext, FragmentManager fm) {
        super(fm);
        fContext = acontext;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new FutsalNewRequest();
        } else if (i == 1){
            return new FutsalBookedFragment();
        } else {
            return new FutsalHistoryFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int i) {
        // Generate title based on item position
        switch (i) {
            case 0:
                return fContext.getString(R.string.new_request);
            case 1:
                return fContext.getString(R.string.booked);
            case 2:
                return fContext.getString(R.string.history);
            default:
                return null;
        }
    }
}