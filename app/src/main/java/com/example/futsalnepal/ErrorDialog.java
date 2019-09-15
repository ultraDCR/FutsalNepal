package com.example.futsalnepal;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;

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
