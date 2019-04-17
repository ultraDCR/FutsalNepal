package com.example.futsalnepal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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


public class FutsalInfoEdit extends AppCompatActivity {

    private CircleImageView fProfilePic;
    private Button saveBtn;
    private EditText fName, fAddress, fPhone, fOpenTime, fCloseTime, fWeakPriceM, fWeakPriceD, fWeakPriceE, fWeakendPriceM, fWeakendPriceD, fWeakendPriceE ;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fDatabase;
    private StorageReference fStorage;
    private String user_id;
    private Uri mainImageURI = null;
    private Bitmap compressedImageFile;
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_info_edit);

        fAuth = FirebaseAuth.getInstance();
        user_id = fAuth.getCurrentUser().getUid();

        fDatabase = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance().getReference();

        fName = findViewById(R.id.orgination_name);
        fAddress = findViewById(R.id.futsal_address);
        fPhone = findViewById(R.id.futsal_number);
        fOpenTime = findViewById(R.id.opening_time);
        fCloseTime = findViewById(R.id.closing_time);
        fWeakPriceM = findViewById(R.id.wd_morning_price);
        fWeakPriceD = findViewById(R.id.wd_day_price);
        fWeakPriceE = findViewById(R.id.wd_evening_price);
        fWeakendPriceM = findViewById(R.id.we_morning_price);
        fWeakendPriceD = findViewById(R.id.we_day_price);
        fWeakendPriceE = findViewById(R.id.we_evening_price);
        fProfilePic = findViewById(R.id.futsal_profile_pic);
        saveBtn = findViewById(R.id.f_save_btn);
        saveBtn.setEnabled(false);

        fDatabase.collection("futsal_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("futsal_name");
                        String image = task.getResult().getString("futsal_logo");
                        String address = task.getResult().getString("futsal_address");
                        String phone = task.getResult().getString("futsal_phone");
                        String open_time = task.getResult().getString("opening_hour");
                        String close_time = task.getResult().getString("closing_hour");
                        String week_days_price = task.getResult().getData().toString();


                        mainImageURI = Uri.parse(image);

                        fName.setText(name);
                        fAddress.setText(address);
                        fPhone.setText(phone);
                        fOpenTime.setText(open_time);
                        fCloseTime.setText(close_time);
                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.profile_image);

                        Glide.with(FutsalInfoEdit.this).setDefaultRequestOptions(placeholderRequest).load(image).into(fProfilePic);


                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(FutsalInfoEdit.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }
                saveBtn.setEnabled(true);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = fName.getText().toString();

                if (!TextUtils.isEmpty(user_name) && mainImageURI != null) {

                    if (isChanged) {

                        user_id = fAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(FutsalInfoEdit.this)
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
                        StorageReference ref = fStorage.child("futsal_image").child(user_id + ".jpg");
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
                                    storeFirestore(task, user_name);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(FutsalInfoEdit.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
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
//                                    Toast.makeText(FutsalInfoEdit.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
//
//
//                                }
//                            }
//                        });

                    } else {

                        storeFirestore(null, user_name);

                    }

                }

            }

        });

        fProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(FutsalInfoEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(FutsalInfoEdit.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(FutsalInfoEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });


    }

    private void storeFirestore(@NonNull Task<Uri> task, String user_name) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("image", download_uri.toString());

        fDatabase.collection("futsal_list").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(FutsalInfoEdit.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(FutsalInfoEdit.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(FutsalInfoEdit.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }


            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(FutsalInfoEdit.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                fProfilePic.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}
