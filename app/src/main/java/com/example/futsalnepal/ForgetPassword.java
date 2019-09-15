package com.example.futsalnepal;

import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reset_email);
        send = findViewById(R.id.send_email);



        send.setOnClickListener( view ->{
            String emailAddress = email.getText().toString();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            new AlertDialog.Builder(ForgetPassword.this)
                                    .setMessage("Email sent successfully. Check your email for for password reser link.")
                                    .show();

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //...here i'm waiting 5 seconds before hiding the custom dialog
                                    //...you can do whenever you want or whenever your work is done
                                    finish();
                                }
                            }, 5000);
                            Log.d("FirebasePasswordReset", "Email sent.");
                        }
                    }).addOnFailureListener(e ->
                        new AlertDialog.Builder(ForgetPassword.this)
                                .setTitle("Error")
                                .setMessage(e.getMessage())
                                .show());
        });
    }
}
