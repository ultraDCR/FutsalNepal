package com.example.futsalnepal;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class TestLayout extends FragmentPagerAdapter {


    public TestLayout(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new LoginFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";

            case 1:
                return "SignUp";

        }
        return null;
    }

}
