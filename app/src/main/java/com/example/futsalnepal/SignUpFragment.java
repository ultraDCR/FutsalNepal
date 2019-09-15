package com.example.futsalnepal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
public class  SignUpFragment extends Fragment {
    private Loading loading;
    private ErrorDialog error;
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
        loading = new Loading(getActivity());
        error = new ErrorDialog(getActivity());

        usignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.showDialog();
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
                            error.showDialog("Password must contain more than 6 character with atleast one uppercase, one special character and no space.");
//                            Toast.makeText(context, "Password must contain more than 6 character with atleast one uppercase, one special character and no space.",
//                                    Toast.LENGTH_LONG).show();
                            loading.hideDialog();
                        }
                    }
                    else{
                        loading.hideDialog();
                        cpass.setError("Doesn't match with password");
                    }
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(confirm_password) || type.equals("None")) {
                    loading.hideDialog();
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
                        error.showDialog("User should not be none.");
//                        Toast.makeText(getContext(),"User should not be none.",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    loading.hideDialog();
                    error.showDialog("Some error occure. Check you information and try again.");
//                    Toast.makeText(context, "Some error occure. Check you information and try again.",
//                            Toast.LENGTH_SHORT).show();
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
                                            loading.hideDialog();
                                            Intent futsalUserIntent = new Intent(context, UserInfoEdit.class);
                                            context.startActivity(futsalUserIntent);
                                            ((Activity) context).finish();

                                        } else {
                                            loading.hideDialog();
                                            String e = task.getException().getLocalizedMessage();
                                            error.showDialog("FIRESTORE Error : " + e);
//                                            Toast.makeText(context, "FIRESTORE Error : " + e, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }else{
                                fDatabase.collection("futsal_list").document(user_id).set(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            loading.hideDialog();
                                            Intent futsalUserIntent = new Intent(context, FutsalInfoEdit.class);
                                            context.startActivity(futsalUserIntent);
                                            ((Activity) context).finish();

                                        } else {
                                            loading.hideDialog();
                                            String e = task.getException().getLocalizedMessage();
                                            error.showDialog("FIRESTORE Error : " + e);
//                                            Toast.makeText(context, "(FIRESTORE Error) : " + e, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }

//                        } else {
//                            loading.hideDialog();
//                            // If sign in fails, display a message to the user.
//                            error.showDialog("Authentication failed."+task.getException().getLocalizedMessage());
////                            Toast.makeText(context, "Authentication failed."+task.getException().getLocalizedMessage(),
////                                    Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.hideDialog();
                error.showDialog(e.getLocalizedMessage());
//                Toast.makeText(context, "Connection failed."+e.getLocalizedMessage(),
//                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
