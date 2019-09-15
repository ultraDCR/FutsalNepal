package com.example.futsalnepal;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.futsalnepal.futsal.FutsalInfoFragment;

public class FutsalDetailViewPageAdapter  extends FragmentPagerAdapter {

    private Context mContext;

    public FutsalDetailViewPageAdapter (Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new BookTimeFragment();
        } else if (position == 1){
            return new FutsalInfoFragment();
        } else {
            return new RatingReviewFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.book_time);
            case 1:
                return mContext.getString(R.string.futsal_info);
            case 2:
                return mContext.getString(R.string.rating_review);
            default:
                return null;
        }
    }

}

