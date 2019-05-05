package com.example.futsalnepal.Model;

import java.util.List;

public class SectionModel {

    private String sectionLabel;
    private List<Booking> futsals;

    public SectionModel(String sectionLabel, List<Booking> futsals) {
        this.sectionLabel = sectionLabel;
        this.futsals = futsals;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public List<Booking> getFutsals() {
        return futsals;
    }

}
