package com.example.futsalnepal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginSignupFragmentPagerAdapter extends PagerAdapter {
    private Context context;
//    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;

    public LoginSignupFragmentPagerAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        int resId ;
        switch (position) {
            case 0:
                resId = R.layout.fragment_login;
                break;

            case 1:
                resId = R.layout.fragment_sign_up;
                break;

            default:
                resId = R.layout.fragment_login;
        }
        //return new LoginFragment();
        Log.d( "instantiateItem:","resid : "+resId+"position : "+position);

        View layout = this.layoutInflater.inflate( resId, collection, false);
        if(position == 0){
            LoginDataHandler(layout);

        }else if(position == 1){
            SignupDataHandler(layout);
        }

        collection.addView(layout);
        return layout;
    }




    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return this.context.getString(R.string.Login);

            case 1:
                return this.context.getString(R.string.SignUp);
            default:
                return null;
        }
    }


    private void LoginDataHandler(View layout) {

        EditText name = layout.findViewById(R.id.login_email_field);
        String hello = name.getText().toString();
        System.out.print(hello);
    }

    private void SignupDataHandler(View layout) {
    }
}
