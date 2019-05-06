package com.example.futsalnepal.Model;

import java.util.List;

public class SectionModel {

    private String sectionLabel;
    private List<BookingFutsal> array;

    public SectionModel(String sectionLabel, List<BookingFutsal> array) {
        this.sectionLabel = sectionLabel;
        this.array = array;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public List<BookingFutsal> getArray() {
        return array;
    }

}
