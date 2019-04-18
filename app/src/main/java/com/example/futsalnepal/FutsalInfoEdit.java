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
import android.util.Log;
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
        user_id = fAuth.getCurrentUser().getUid();

        fDatabase.collection("futsal_list").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        if (task.getResult().getString("futsal_name") != null) {
                            Log.d("TestingData", "onComplete: " + task.getResult().get("week_end_price"));
                            String name = task.getResult().getString("futsal_name");
                            String image = task.getResult().getString("futsal_logo");
                            String address = task.getResult().getString("futsal_address");
                            String phone = task.getResult().getString("futsal_phone");
                            String open_time = task.getResult().getString("opening_hour");
                            String close_time = task.getResult().getString("closing_hour");
                            Map<String, String> week_price = (Map<String, String>) task.getResult().get("week_end_price");
                            String w_morning = week_price.get("morning_price");
                            String w_day = week_price.get("day_price");
                            String w_evening = week_price.get("evening_price");
                            String we_morning = week_price.get("morning_price");
                            String we_day = week_price.get("day_price");
                            String we_evening = week_price.get("evening_price");


                            mainImageURI = Uri.parse(image);

                            fName.setText(name);
                            fAddress.setText(address);
                            fPhone.setText(phone);
                            fOpenTime.setText(open_time);
                            fCloseTime.setText(close_time);
                            fWeakPriceM.setText(w_morning);
                            fWeakPriceD.setText(w_day);
                            fWeakPriceE.setText(w_evening);
                            fWeakendPriceM.setText(we_morning);
                            fWeakendPriceD.setText(we_day);
                            fWeakendPriceE.setText(we_evening);

                            RequestOptions placeholderRequest = new RequestOptions();
                            placeholderRequest.placeholder(R.drawable.profile_image);

                            Glide.with(FutsalInfoEdit.this).setDefaultRequestOptions(placeholderRequest).load(image).into(fProfilePic);
                        }

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

                final String futsal_name = fName.getText().toString();
                final String futsal_address = fAddress.getText().toString();
                final String futsal_phone = fPhone.getText().toString();
                final String opening_hour = fOpenTime.getText().toString();
                final String closing_hour = fCloseTime.getText().toString();
                final String week_price_m = fWeakPriceM.getText().toString();
                final String week_price_d = fWeakPriceD.getText().toString();
                final String week_price_e = fWeakPriceE.getText().toString();
                final String week_end_price_m = fWeakendPriceM.getText().toString();
                final String week_end_price_d = fWeakendPriceD.getText().toString();
                final String week_end_price_e = fWeakendPriceE.getText().toString();




                if (!TextUtils.isEmpty(futsal_name) && mainImageURI != null && !TextUtils.isEmpty(futsal_address)
                        && !TextUtils.isEmpty(futsal_phone) && !TextUtils.isEmpty(opening_hour)
                        && !TextUtils.isEmpty(closing_hour) && !TextUtils.isEmpty(week_end_price_m)
                        && !TextUtils.isEmpty(week_price_d) && !TextUtils.isEmpty(week_price_e)
                        && !TextUtils.isEmpty(week_price_m) && !TextUtils.isEmpty(week_end_price_d) && !TextUtils.isEmpty(week_end_price_e)
                ) {

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
                        StorageReference ref = fStorage.child("futsal_pic_list").child(user_id).child("logo").child("logo.png");
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
                                    storeFirestore(task, futsal_name, futsal_address,
                                            futsal_phone,opening_hour,closing_hour,
                                            week_price_m,week_price_d,week_price_e,
                                            week_end_price_m, week_end_price_d,week_end_price_e
                                            );
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

                        storeFirestore(null, futsal_name, futsal_address,
                                futsal_phone,opening_hour,closing_hour,
                                week_price_m,week_price_d,week_price_e,
                                week_end_price_m, week_end_price_d,week_end_price_e
                                );

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

    private void storeFirestore(@NonNull Task<Uri> task, String futsal_name, String  futsal_address,
                                String futsal_phone, String opening_hour, String closing_hour,
                                String week_price_m, String week_price_d, String week_price_e,
                                String week_end_price_m, String week_end_price_d, String week_end_price_e) {

        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, Object> futsalMap = new HashMap<>();
        futsalMap.put("futsal_name", futsal_name);
        futsalMap.put("futsal_logo", download_uri.toString());
        futsalMap.put("futsal_address",futsal_address);
        futsalMap.put("futsal_phone",futsal_phone);
        futsalMap.put("opening_hour", opening_hour);
        futsalMap.put("closing_hour", closing_hour);

        Map<String, Object> week_price = new HashMap<>();
        week_price.put("morning_price", week_price_m);
        week_price.put("day_price", week_price_d);
        week_price.put("evening_price", week_price_e);

        futsalMap.put("week_day_price", week_price);

        Map<String, Object> week_end_price = new HashMap<>();
        week_end_price.put("morning_price", week_end_price_m);
        week_end_price.put("day_price", week_end_price_d);
        week_end_price.put("evening_price", week_end_price_e);

        futsalMap.put("week_end_price", week_end_price);


        fDatabase.collection("futsal_list").document(user_id).set(futsalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
