package com.example.futsalnepal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.futsalnepal.futsal.FutsalInfoEdit;
import com.example.futsalnepal.users.UserInfoEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Context context;
    private FirebaseAuth mAuth;
    private int resId = 0;
    private FirebaseFirestore fDatabase;
    //    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;

    public SignUpFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        context = getActivity();
        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();
        EditText name = view.findViewById(R.id.signup_email_field);
        EditText pass = view.findViewById(R.id.signup_password_field);
        EditText cpass = view.findViewById(R.id.signup_confirm_pass);
        RadioGroup signup_type_radio = view.findViewById(R.id.radio_group);
        Button usignup = view.findViewById(R.id.signup_btn);
        ProgressBar signup_prog = view.findViewById(R.id.signup_pbar);

        usignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_prog.setVisibility(View.VISIBLE);
                String email = name.getText().toString();
                String password = pass.getText().toString();
                String passPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}";
                String confirm_password = cpass.getText().toString();
                String type="None";

                int selectedId=signup_type_radio.getCheckedRadioButtonId();
                RadioButton rBtn=view.findViewById(selectedId);
                String usertype = rBtn.getText().toString();

                if(usertype.equals("User")){
                    type= "users_list";
                }else if(usertype.equals("Futsal Owner")){
                    type = "futsal_list";
                }
                Log.d("SwitchOn", "onClick: "+type);
                if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password) &&!TextUtils.isEmpty(confirm_password) && !type.equals("None")){
                    if(password.equals(confirm_password)){
                        if(password.matches(passPattern)){
                            signup_userId(email,password,type,signup_prog);
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
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirm_password) || type.equals("None")) {
                    signup_prog.setVisibility(View.GONE);
                    if(TextUtils.isEmpty(email)){
                        name.setError("Field required");
                    }
                    if(TextUtils.isEmpty(password)){
                        pass.setError("Field required");
                    }
                    if(TextUtils.isEmpty(confirm_password)){
                        cpass.setError("Field required");
                    }
                    if(usertype.equals("None")){
                        Toast.makeText(getContext(),"User should not be none.",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    signup_prog.setVisibility(View.GONE);
                    Toast.makeText(context, "Some error occure. Check you information and try again.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;
    }


    public void  signup_userId(String email, String password,String userType,ProgressBar signup_prog) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String user_id = mAuth.getCurrentUser().getUid();
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("created_at", FieldValue.serverTimestamp());
                            if(userType.equals("users_list")) {
                                fDatabase.collection("users_list").document(user_id).set(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
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
