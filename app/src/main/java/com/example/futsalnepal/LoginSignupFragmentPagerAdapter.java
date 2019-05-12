package com.example.futsalnepal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginSignupFragmentPagerAdapter extends PagerAdapter {
    private Context context;
    private FirebaseAuth mAuth;
    private int resId = 0;
    private FirebaseFirestore fDatabase;
//    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;

    public LoginSignupFragmentPagerAdapter(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();
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

        switch (position) {
            case 0:
                resId = R.layout.fragment_login;
                break;

            case 1:
                resId = R.layout.fragment_sign_up;
                break;
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

        }
        return null;
    }


    private void LoginDataHandler(View layout) {
        EditText name = layout.findViewById(R.id.login_email_field);
        EditText pass = layout.findViewById(R.id.login_password_field);
        Button ulogin = layout.findViewById(R.id.login_btn);
        Switch logintype = layout.findViewById(R.id.login_switch);
        ProgressBar prgBar = layout.findViewById(R.id.login_pbar);


        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prgBar.setVisibility(View.VISIBLE);
                String email = name.getText().toString();
                String password = pass.getText().toString();
                String userType;
                if(logintype.isChecked()){
                    userType = "futsal_list";
                }else {
                    userType = "user_list";
                }
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    login_userId(email,password,userType,prgBar);
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    prgBar.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(email)){
                        name.setError("Field required");
                    }
                    if(TextUtils.isEmpty(password)){
                        pass.setError("Field required");
                    }
                } else {
                    prgBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Some error occur. Check you information and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void SignupDataHandler(View layout) {
        EditText name = layout.findViewById(R.id.signup_email_field);
        EditText pass = layout.findViewById(R.id.signup_password_field);
        EditText cpass = layout.findViewById(R.id.signup_confirm_pass);
        Switch signup_type = layout.findViewById(R.id.signup_switch);
        Button usignup = layout.findViewById(R.id.signup_btn);
        ProgressBar signup_prog = layout.findViewById(R.id.signup_pbar);

        usignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_prog.setVisibility(View.VISIBLE);
                String email = name.getText().toString();
                String password = pass.getText().toString();
                String passPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
                String confirm_password = cpass.getText().toString();
                String userType;
                if(signup_type.isChecked()){
                    userType = "futsal_list";
                }else {
                    userType = "user_list";
                }
                Log.d("SwitchOn", "onClick: "+userType);
                if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password) &&!TextUtils.isEmpty(confirm_password)){
                    if(password.equals(confirm_password)){
                        if(password.matches(passPattern)){
                            signup_userId(email,password,userType,signup_prog);
                        }
                        else{
                            Toast.makeText(context, "Password must contain more than 6 character with atleast one uppercase, one special character and no space.",
                                    Toast.LENGTH_LONG).show();
                            signup_prog.setVisibility(View.GONE);

                        }
                    }
                    else{
                        signup_prog.setVisibility(View.GONE);
                        cpass.setError("Doesn't match with password");
                    }
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirm_password)) {
                    signup_prog.setVisibility(View.GONE);
                    name.setError("Field required");
                    pass.setError("Field required");
                    cpass.setError("Field required");
                } else {
                    signup_prog.setVisibility(View.GONE);
                    Toast.makeText(context, "Some error occure. Check you information and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }





    public void  login_userId(String email, String password,String type,ProgressBar prg_layout){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String user_id = mAuth.getCurrentUser().getUid();
                            fDatabase.collection(type).document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {

                                        if (task.getResult().exists()) {
                                            if(type.equals("user_list")) {
                                                Intent signin = new Intent(context, MainActivity.class);
                                                context.startActivity(signin);
                                                ((Activity) context).finish();
                                            }else if(type.equals("futsal_list")){
                                                Intent signin = new Intent(context, FutsalHome.class);
                                                context.startActivity(signin);
                                                ((Activity) context).finish();
                                            }
                                        }else{
                                            prg_layout.setVisibility(View.GONE);
                                            mAuth.signOut();
                                            Toast.makeText(context, "Wrong user type. Try switching user type and try again to login.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        prg_layout.setVisibility(View.GONE);
                                        Toast.makeText(context, "Something went wrong",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            prg_layout.setVisibility(View.GONE);
                            Log.w("signIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void  signup_userId(String email, String password,String userType,ProgressBar signup_prog) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String user_id = mAuth.getCurrentUser().getUid();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("created_at", FieldValue.serverTimestamp());
                            if(userType.equals("user_list")) {
                                fDatabase.collection("user_list").document(user_id).set(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            signup_prog.setVisibility(View.GONE);
                                            Intent futsalUserIntent = new Intent(context, UserInfoEdit.class);
                                            context.startActivity(futsalUserIntent);
                                            ((Activity) context).finish();

                                        } else {
                                            signup_prog.setVisibility(View.GONE);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(context, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }else{
                                fDatabase.collection("futsal_list").document(user_id).set(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            signup_prog.setVisibility(View.GONE);
                                            Intent futsalUserIntent = new Intent(context, FutsalInfoEdit.class);
                                            context.startActivity(futsalUserIntent);
                                            ((Activity) context).finish();

                                        } else {
                                            signup_prog.setVisibility(View.GONE);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(context, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }

                        } else {
                            signup_prog.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed."+task.getException(),
                                    Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                signup_prog.setVisibility(View.GONE);
                Toast.makeText(context, "Connection failed."+e,
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
