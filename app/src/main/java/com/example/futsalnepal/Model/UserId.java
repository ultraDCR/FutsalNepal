package com.example.futsalnepal.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class UserId {
    @Exclude
    public String userId;

    public <T extends UserId> T withId(@NonNull final String userId){

        this.userId = userId;
        return  (T) this;

    }
}
