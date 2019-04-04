package com.example.futsalnepal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginSignupFragmentPagerAdapter extends PagerAdapter {
    private Context mContext;

    int resId = 0;

    public LoginSignupFragmentPagerAdapter(Context context) {
        mContext = context;
    }



    public Object instantiateItem(ViewGroup collection, int position) {


        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (position) {
            case 0:
                resId = R.layout.fragment_login;
                break;

            case 1:
                resId = R.layout.fragment_sign_up;
                break;
        }
        //return new LoginFragment();
        ViewGroup layout = (ViewGroup) inflater.inflate(resId, collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.Login);

            case 1:
                return mContext.getString(R.string.SignUp);
            default:
                return null;
        }
    }

}
