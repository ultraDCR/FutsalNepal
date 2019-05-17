package com.example.futsalnepal;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowManager;

public class LoginDialog  {
    Context context;
    Activity activity;
    public LoginDialog(Context context, Activity activity){
            this.context = context;
            this.activity = activity;

//        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialog));
//
////        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.login_signup_dialog, null);
//        builder.setView(dialogView);
//
//        LoginSignupFragmentPagerAdapter adapter = new LoginSignupFragmentPagerAdapter(context);
//        ViewPager viewPager = dialogView.findViewById(R.id.login_signup_view);
//        TabLayout tabLayout =  dialogView.findViewById(R.id.login_sign_maintab);
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setAdapter(adapter);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void startLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.CustomAlertDialog));
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_signup_dialog, null);
        builder.setView(dialogView);

        LoginSignupFragmentPagerAdapter adapter = new LoginSignupFragmentPagerAdapter(context);
        ViewPager viewPager = dialogView.findViewById(R.id.login_signup_view);
        TabLayout tabLayout =  dialogView.findViewById(R.id.login_sign_maintab);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.show();
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

}
