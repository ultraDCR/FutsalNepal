package com.example.futsalnepal;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

public class ContactUs extends AppCompatActivity {
    private EditText message,email,name;
    private Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.sender_name);
        email = findViewById(R.id.sender_email);
        message = findViewById(R.id.sender_message);
        send = findViewById(R.id.send_btn);

        send.setOnClickListener(v -> {
            sendMail();
        });

    }

    private void sendMail() {
        String subject = name.getText().toString();
        String emailAddress = email.getText().toString();
        String mesg = message.getText().toString();
        String[] TO = {"futsaltimeofficial@gmail.com"};
        String[] CC = {emailAddress};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_CC,CC);
        intent.putExtra(Intent.EXTRA_EMAIL, TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, mesg);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));

    }

    // for toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}