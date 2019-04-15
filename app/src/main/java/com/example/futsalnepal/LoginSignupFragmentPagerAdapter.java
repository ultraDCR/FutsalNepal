package com.example.futsalnepal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginSignupFragmentPagerAdapter extends PagerAdapter {
    private Context context;
    private FirebaseAuth mAuth;
//    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;

    public LoginSignupFragmentPagerAdapter(Context context,FirebaseAuth mAuth) {
        this.context = context;
        this.mAuth = mAuth;
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
        EditText pass = layout.findViewById(R.id.login_password_field);
        Button ulogin = layout.findViewById(R.id.login_btn);


        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = name.getText().toString();
                String password = pass.getText().toString();
                if (!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){

                    login_userId(email,password);
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    name.setError("Field required");
                    pass.setError("Field required");
                } else {
                    Toast.makeText(context, "Some error occure. Check you information and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void SignupDataHandler(View layout) {
        EditText name = layout.findViewById(R.id.signup_email_field);
        EditText pass = layout.findViewById(R.id.signup_password_field);
        EditText cpass = layout.findViewById(R.id.signup_confirm_pass);
        Button usignup = layout.findViewById(R.id.signup_btn);

        usignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = name.getText().toString();
                String password = pass.getText().toString();
                String confirm_password = cpass.getText().toString();
                if (!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password) ||!TextUtils.isEmpty(confirm_password)){
                    if(password.equals(confirm_password)){
                        signup_userId(email,password);
                    }
                    else{
                        cpass.setError("Doesn't match with password");
                    }
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirm_password)) {
                    name.setError("Field required");
                    pass.setError("Field required");
                    cpass.setError("Field required");
                } else {
                    Toast.makeText(context, "Some error occure. Check you information and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }





    public void  login_userId(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent signin = new Intent(context,MainActivity.class);
                            signin.putExtra("futsal_name", user);
                            context.startActivity(signin);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void  signup_userId(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signup", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent signup = new Intent(context,MainActivity.class);
                            signup.putExtra("User", user);
                            context.startActivity(signup);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signup", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });

    }
}
