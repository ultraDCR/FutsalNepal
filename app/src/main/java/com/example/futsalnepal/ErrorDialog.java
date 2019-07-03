package com.example.futsalnepal;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

public class ErrorDialog {
    private  Activity activity;
    public ErrorDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog(String message){
        new AlertDialog.Builder(activity)
                .setTitle("Error")
                .setMessage(message)
                .setNegativeButton("Ok", (dialog, which) -> {
                    //Toast.makeText(FutsalIndivisualDetails.this, "You Clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }
}
