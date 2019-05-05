package com.example.futsalnepal.Model;

import java.util.List;

public class SectionModel {

    private String sectionLabel;
    private List<Futsal> futsals;

    public SectionModel(String sectionLabel, List<Futsal> futsals) {
        this.sectionLabel = sectionLabel;
        this.futsals = futsals;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public List<Futsal> getFutsals() {
        return futsals;
    }

}
