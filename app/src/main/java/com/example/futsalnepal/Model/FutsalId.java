package com.example.futsalnepal.Model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class FutsalId {
    @Exclude
    public String FutsalId;

    public <T extends FutsalId> T withId(@NonNull final String id){

        this.FutsalId = id;
        return  (T) this;

    }
}
