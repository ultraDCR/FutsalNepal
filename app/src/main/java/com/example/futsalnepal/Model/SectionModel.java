package com.example.futsalnepal.Model;

import java.util.List;

public class SectionModel {

    private String sectionLabel;
    private List<BookingFutsal> futsalArray;
    private List<BookingUser> userArray;

    public SectionModel(String sectionLabel,List<BookingFutsal> futsalArray, List<BookingUser> userArray) {
        this.sectionLabel = sectionLabel;
        this.futsalArray = futsalArray;
        this.userArray = userArray;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public List<BookingFutsal> getFutsalArray() {
        return futsalArray;
    }

    public List<BookingUser> getUserArray() {
        return userArray;
    }
}
