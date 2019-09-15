package com.example.futsalnepal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.futsalnepal.futsal.FutsalHome;
import com.example.futsalnepal.futsal.FutsalInfoEdit;
import com.example.futsalnepal.users.UserInfoEdit;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private Loading loading;
    private Context context;
    private FirebaseAuth mAuth;
    private int resId = 0;
    private String TAG = "LoginFragment";
    private FirebaseFirestore fDatabase;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressBar prg_layout;
    //    private List<DataObject> dataObjectList;
    private LayoutInflater layoutInflater;
    private String type;
    private RadioGroup rGroup;
    private RadioButton rBtn;
    private View view;
    private LoginButton fblogin;
    private ImageView fbBtn;
    private ErrorDialog error;
    private CallbackManager mCallbackManager;

    public LoginFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view for this fragment
        view =  inflater.inflate(R.layout.fragment_login, container, false);
        context = getActivity();
        mAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseFirestore.getInstance();

        loading = new Loading(getActivity());

        error = new ErrorDialog(getActivity());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);
        TextView forget_password = view.findViewById(R.id.forget_password);
        EditText name = view.findViewById(R.id.login_email_field);
        EditText pass = view.findViewById(R.id.login_password_field);
        Button ulogin = view.findViewById(R.id.login_btn);
        rGroup = view.findViewById(R.id.radio_group);
        prg_layout = view.findViewById(R.id.login_pbar);
        ImageView googleLogin = view.findViewById(R.id.login_google);
        fblogin = view.findViewById(R.id.fb_login);
        fbBtn=  view.findViewById(R.id.fb_btn);

        fbBtn.setOnClickListener(v ->{
            int selectedId=rGroup.getCheckedRadioButtonId();
            rBtn=view.findViewById(selectedId);
            String usertype = rBtn.getText().toString();
            if(!usertype.equals("None")) {
                if(usertype.equals("User")){
                    type= "users_list";
                }else if(usertype.equals("Futsal Owner")){
                    type = "futsal_list";
                }
                fblogin.performClick();
            }else{
                error.showDialog("User should not be none.");
                //Toast.makeText(getContext(),"User should not be none.",Toast.LENGTH_SHORT).show();
            }
        });

        //forget pasword
        forget_password.setOnClickListener(v -> {
            Intent forgotIntent = new Intent(context, ForgetPassword.class);
            // start the new activity
            startActivity(forgotIntent);

        });


        //Facebook Login
        mCallbackManager = CallbackManager.Factory.create();
        fblogin.setReadPermissions("email", "public_profile");
        fblogin.setFragment(this);
        fblogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FACEBOOK1", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK12", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK13", "facebook:onError", error);
                // ...
            }
        });

        //google login
        googleLogin.setOnClickListener(v->{

            int selectedId=rGroup.getCheckedRadioButtonId();
            rBtn=view.findViewById(selectedId);
            String usertype = rBtn.getText().toString();
            if(!usertype.equals("None")) {
                if(usertype.equals("User")){
                    type= "users_list";
                }else if(usertype.equals("Futsal Owner")){
                    type = "futsal_list";
                }
                googleSignIn();
            }else{
                error.showDialog("User should not be none.");
//                Toast.makeText(getContext(),"User should not be none.",Toast.LENGTH_SHORT).show();
            }

        });

        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                prg_layout.setVisibility(View.VISIBLE);
                loading.showDialog();
                String email = name.getText().toString();
                String password = pass.getText().toString();

                int selectedId=rGroup.getCheckedRadioButtonId();
                rBtn=view.findViewById(selectedId);
                String usertype = rBtn.getText().toString();

                if(usertype.equals("User")){
                    type= "users_list";
                }else if(usertype.equals("Futsal Owner")){
                    type = "futsal_list";
                }

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !usertype.equals("None")){

                    login_userId(email,password);
                }
                else if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || usertype.equals("None")) {
                    loading.hideDialog();
                    if(TextUtils.isEmpty(email)){
                        name.setError("Field required");
                    }
                    if(TextUtils.isEmpty(password)){
                        pass.setError("Field required");
                    }
                    if(usertype.equals("None")){
                        error.showDialog("User should not be none.");
                        //Toast.makeText(getContext(),"User should not be none.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loading.hideDialog();
                    error.showDialog("Some error occur. Check you information and try again.");

                   // Toast.makeText(getContext(), "Some error occur. Check you information and try again.",
                           // Toast.LENGTH_SHORT).show();
                }
            }

        });
        return view;
    }


    public void  login_userId(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String user_id = mAuth.getCurrentUser().getUid();

                            redirectOnSuccess(type,user_id);


                        } else {
                            // If sign in fails, display a message to the user.
                            loading.hideDialog();
                            Log.w("signIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }



    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+requestCode+" "+resultCode+" "+data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode == 64206){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null)
                    firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignInError", "Google sign in failed", e);
                // ...
            }
        }
        // Pass the activity result back to the Facebook SDK

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("GoogleSignIn", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            boolean isfirst = task.getResult().getAdditionalUserInfo().isNewUser();
                            loading.showDialog();
                            if(isfirst){
                                googleSignUp(user);
                            }else{
                                redirectOnSuccess(type,user.getUid());
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERRorGoogleSignin", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void redirectOnSuccess(String type,String user_id){
    fDatabase.collection(type).document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            if (task.isSuccessful()) {

                if (task.getResult().exists()) {
                    if(type.equals("users_list")) {
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                String token_id = task.getResult().getToken();
                                Map<String,Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id",token_id);
                                fDatabase.collection("users_list").document(user_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loading.hideDialog();
                                        Intent signin = new Intent(context, MainActivity.class);
                                        context.startActivity(signin);
                                        ((Activity) context).finish();
                                    }
                                });

                            }
                        });
                    }else if(type.equals("futsal_list")){

                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                                String token_id = task.getResult().getToken();
                                Map<String,Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id",token_id);
                                fDatabase.collection("futsal_list").document(user_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loading.hideDialog();
                                        Intent signin = new Intent(context, FutsalHome.class);
                                        context.startActivity(signin);
                                        ((Activity) context).finish();
                                    }
                                });
                            }
                        });
                    }
                }else{
                    loading.hideDialog();
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();
                    error.showDialog("Wrong user type. Try switching user type and try again to login.");
//                    Toast.makeText(context, "Wrong user type. Try switching user type and try again to login.",
//                            Toast.LENGTH_SHORT).show();
                }

            } else {
                loading.hideDialog();
                error.showDialog("Something went wrong");
//                Toast.makeText(context, "Something went wrong",
//                        Toast.LENGTH_SHORT).show();

            }
        }
    });
}


    private void googleSignUp(FirebaseUser user){

            String name = user.getDisplayName();
            String email = user.getEmail();
            String photoUrl = user.getPhotoUrl().toString();
            Log.d(TAG, "googleSignUp: "+user.getProviderId());
            Toast.makeText(context, ""+user.getProviderId(), Toast.LENGTH_SHORT).show();
            photoUrl = photoUrl + "?width=150&height=150";
            String user_id = user.getUid();
            Map<String, Object> uMap = new HashMap<>();
            uMap.put("created_at", FieldValue.serverTimestamp());
            uMap.put("user_full_name", name);
            uMap.put("user_email", email);
            uMap.put("user_profile_image", photoUrl);

            Map<String, Object> fMap = new HashMap<>();
            fMap.put("created_at", FieldValue.serverTimestamp());
            fMap.put("futsal_name", name);
            fMap.put("futsal_email", email);
            fMap.put("futsal_logo", photoUrl);
            if (type.equals("users_list")) {
                fDatabase.collection("users_list").document(user_id).set(uMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.hideDialog();
                            Intent futsalUserIntent = new Intent(context, UserInfoEdit.class);
                            context.startActivity(futsalUserIntent);
                            ((Activity) context).finish();

                        } else {
                            loading.hideDialog();
                            String e = task.getException().getMessage();
                            error.showDialog("FIRESTORE Error : " + e);
                            //Toast.makeText(context, "(FIRESTORE Error) : " + e, Toast.LENGTH_LONG).show();

                        }
                    }
                });

            } else {
                fDatabase.collection("futsal_list").document(user_id).set(fMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.hideDialog();
                            Intent futsalUserIntent = new Intent(context, FutsalInfoEdit.class);
                            context.startActivity(futsalUserIntent);
                            ((Activity) context).finish();

                        } else {
                            loading.hideDialog();
                            String e = task.getException().getMessage();
                            error.showDialog("FIRESTORE Error : " + e);
                            //Toast.makeText(context, "(FIRESTORE Error) : " + e, Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
    }


    //facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            boolean isfirst = task.getResult().getAdditionalUserInfo().isNewUser();
                            loading.showDialog();
                            if(isfirst){
                                googleSignUp(user);
                            }else{
                                redirectOnSuccess(type,user.getUid());
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            error.showDialog("Authentication failed : " + task.getException().getLocalizedMessage());
//                            Toast.makeText(getContext(), "Authentication failed :" +task.getException(),
//                                    Toast.LENGTH_SHORT).show();
                            LoginManager.getInstance().logOut();

                        }

                        // ...
                    }
                });
    }


    public void showCustomLoadingDialog(View view) {

        //..show gif
        loading.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                loading.hideDialog();
            }
        }, 5000);
    }

}