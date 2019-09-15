package com.example.futsalnepal.users;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.futsalnepal.MainActivity;
import com.example.futsalnepal.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class UserInfoEdit extends AppCompatActivity {

    private CircleImageView uProfilePic;
    private Button saveBtn;
    private EditText uName, uAddress, uPhone;
    private FirebaseAuth uAuth;
    private FirebaseFirestore uDatabase;
    private StorageReference uStorage;
    private String user_id;
    private ProgressBar pbar;
    private Uri mainImageURI = null;
    private Bitmap compressedImageFile;
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        uAuth = FirebaseAuth.getInstance();

        uDatabase = FirebaseFirestore.getInstance();
        uStorage = FirebaseStorage.getInstance().getReference();

        pbar  = findViewById(R.id.user_save_pbar);
        uName = findViewById(R.id.user_fullname);
        uAddress = findViewById(R.id.user_address);
        uPhone = findViewById(R.id.user_phone_number);
        uProfilePic = findViewById(R.id.user_profile_pic);
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setEnabled(false);
        user_id = uAuth.getCurrentUser().getUid();
        String user_email = uAuth.getCurrentUser().getEmail();


        uDatabase.collection("users_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        if (task.getResult().getString("user_full_name") != null) {
                            Log.d("TestingData", "onComplete: " + task.getResult().get("week_end_price"));
                            String name = task.getResult().getString("user_full_name");
                            String image = task.getResult().getString("user_profile_image");
                            String address = task.getResult().getString("user_address");
                            String phone = task.getResult().getString("user_phone_number");


                            mainImageURI = Uri.parse(image);

                            uName.setText(name);
                            uAddress.setText(address);
                            uPhone.setText(phone);


                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile_image);

                            Glide.with(UserInfoEdit.this).setDefaultRequestOptions(placeholderRequest).load(image).into(uProfilePic);

                        }
                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(UserInfoEdit.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }
                saveBtn.setEnabled(true);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.GONE);
                pbar.setVisibility(View.VISIBLE);

                final String user_name = uName.getText().toString();
                final String user_address = uAddress.getText().toString();
                final String user_phone = uPhone.getText().toString();

                if (!TextUtils.isEmpty(user_name) && mainImageURI != null && !TextUtils.isEmpty(user_address) && !TextUtils.isEmpty(user_phone))
                {

                    if (isChanged) {

                        user_id = uAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(UserInfoEdit.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();
                        StorageReference ref = uStorage.child("users_pic_list").child(user_id).child("profile_pic").child( "pic.png");
                        UploadTask image_path = ref.putBytes(thumbData);

                        Task<Uri> urlTask = image_path.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, user_name, user_address, user_phone, user_email);
                                } else {
                                    saveBtn.setVisibility(View.VISIBLE);
                                    pbar.setVisibility(View.GONE);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(UserInfoEdit.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
//                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                                if (task.isSuccessful()) {
//                                    task.getResult();
//                                    storeFirestore(task, user_name);
//
//                                } else {
//
//                                    String error = task.getException().getMessage();
//                                    Toast.makeText(UserInfoEdit.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
//
//
//                                }
//                            }
//                        });

                    } else {

                        storeFirestore(null, user_name, user_address, user_phone, user_email);

                    }

                }else{
                    saveBtn.setVisibility(View.VISIBLE);
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(UserInfoEdit.this, "All the fields are required.", Toast.LENGTH_LONG).show();
                }

            }

        });

        uProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(UserInfoEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(UserInfoEdit.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(UserInfoEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });


    }

    private void storeFirestore(@NonNull Task<Uri> task, String user_name, String  user_address, String user_phone, String user_email) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult();

        } else {

            download_uri = mainImageURI;

        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                Map<String, String> userMap = new HashMap<>();
                userMap.put("user_full_name", user_name);
                userMap.put("user_profile_image", download_uri.toString());
                userMap.put("user_address",user_address);
                userMap.put("user_phone_number",user_phone);
                userMap.put("user_email",user_email);
                userMap.put("token_id",task.getResult().getToken());



                uDatabase.collection("users_list").document(user_id).set(userMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            saveBtn.setVisibility(View.VISIBLE);
                            pbar.setVisibility(View.GONE);
                            Toast.makeText(UserInfoEdit.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(UserInfoEdit.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            saveBtn.setVisibility(View.VISIBLE);
                            pbar.setVisibility(View.GONE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserInfoEdit.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                        }


                    }
                });


            }
        });

    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(UserInfoEdit.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                uProfilePic.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                saveBtn.setVisibility(View.VISIBLE);
                pbar.setVisibility(View.GONE);
                Exception error = result.getError();

            }
        }

    }
}
