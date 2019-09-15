package com.example.futsalnepal.futsal;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.futsalnepal.R;
import com.example.futsalnepal.users.BookedFragment;
import com.example.futsalnepal.users.HistoryFragment;
import com.example.futsalnepal.users.PendingFragment;

public class BookInformationUserVpagerAdater extends FragmentPagerAdapter {

    private Context mContext;

    public BookInformationUserVpagerAdater (Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PendingFragment();
        } else if (position == 1){
            return new BookedFragment();
        } else {
            return new HistoryFragment();
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
                return mContext.getString(R.string.pending_time);
            case 1:
                return mContext.getString(R.string.booked);
            case 2:
                return mContext.getString(R.string.history);
            default:
                return null;
        }
    }

}