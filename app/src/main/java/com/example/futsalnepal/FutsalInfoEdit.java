package com.example.futsalnepal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class FutsalInfoEdit extends AppCompatActivity {

    private CircleImageView fProfilePic;
    private Button saveBtn;
    private EditText fName, fAddress, fPhone, fOpenTime, fCloseTime, fWeakPriceM, fWeakPriceD, fWeakPriceE, fWeakendPriceM, fWeakendPriceD, fWeakendPriceE ;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fDatabase;
    private StorageReference fStrage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futsal_info_edit);


    }
}
