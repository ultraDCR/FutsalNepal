package com.example.futsalnepal.Model;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class NotificationId {
    @Exclude
    public String NotificationId;

    public <T extends NotificationId> T withId(@NonNull final String id){

        this.NotificationId = id;
        return  (T) this;

    }
}
